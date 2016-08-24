package nl.sebastiaanschool.contact.app.gui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Period;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.DownloadManagerInterface;
import nl.sebastiaanschool.contact.app.data.downloadmanager.Download;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * RecyclerView adapter for Timeline Items.
 */
class TimelineRVAdapter extends AbstractRVAdapter<TimelineItem, TimelineRVAdapter.ViewHolder> {

    /**
     * Downloads are kept separately from the list items because this makes the Rx flows simpler.
     */
    private final SimpleArrayMap<String, Download> downloads = new SimpleArrayMap<>(20);
    private final PublishSubject<TimelineItem> itemsClicked = PublishSubject.create();
    private final BackendInterface backendApi;
    private final Context context;
    private RecyclerView recyclerView;

    public TimelineRVAdapter(TimelineRVDataSource timelineDataSource, Listener listener,
                             BackendInterface backendApi, final Context context) {
        super(timelineDataSource, listener);
        this.backendApi = backendApi;
        this.context = context;
        subscriptions.add(itemsClicked
                .filter(new Func1<TimelineItem, Boolean>() {
                    @Override
                    public Boolean call(TimelineItem timelineItem) {
                        return timelineItem.type == TimelineItem.TYPE_NEWSLETTER;
                    }
                })
                .map(new Func1<TimelineItem, Download>() {
                    @Override
                    public Download call(TimelineItem timelineItem) {
                        return downloads.get(timelineItem.documentUrl);
                    }
                })
                .subscribe(new Action1<Download>() {
                    @Override
                    public void call(Download download) {
                        switch (download.statusCode) {
                            case Download.STATUS_COMPLETED:
                                //fall-through
                            case Download.STATUS_OPEN_ON_WEB:
                                launch(download);
                                break;
                            case Download.STATUS_PENDING:
                                enqueue(download);
                                break;
                            case Download.STATUS_DOWNLOADING:
                                // TODO confirm first?
                                // TODO this doesn't seem to cancel the notification.
                                remove(download);
                                break;
                            case Download.STATUS_FAILED:
                                Log.d("Timeline", "TODO retry failed download");
                                // TODO retry. Issue some kind of notification first?
                                break;
                            default:
                                // Ignored.
                        }
                    }
                }));
        subscriptions.add(DownloadManagerInterface.getInstance()
                .statusUpdates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadManagerInterface.DownloadEvent>() {
            @Override
            public void call(DownloadManagerInterface.DownloadEvent event) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(event.type)) {
                    Download download = findDownloadByDownloadManagerId(event.downloadManagerId);
                    if (download != null) {
                        // Get up-to-date status info from DownloadManager
                        DownloadManagerInterface.getInstance().findExistingEntry(download)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(downloadStatusObserver);
                    }
                }
            }
        }));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
    }

    @Nullable
    private Download findDownloadByDownloadManagerId(long dmId) {
        for (int i = 0, max = downloads.size(); i < max; i++) {
            Download d = downloads.valueAt(i);
            if (d.downloadManagerId == dmId) {
                return d;
            }
        }
        return null;
    }

    private void enqueue(Download download) {
        subscriptions.add(DownloadManagerInterface.getInstance()
                .enqueueDownload(download)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadStatusObserver));
    }

    private void launch(final Download download) {
        subscriptions.add(download.createOpeningIntent(context)
            .subscribe(new Action1<Intent>() {
                @Override
                public void call(Intent intent) {
                    GrabBag.openUri(context, intent);
                }
            })
        );
    }

    private void remove(Download download) {
        subscriptions.add(DownloadManagerInterface.getInstance()
                .remove(download)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Action1<Download>() {
                    @Override
                    public void call(Download download) {
                        TimelineRVAdapter.this.notify(R.string.download_stopped);
                    }
                })
                .subscribe(downloadStatusObserver));
    }

    private void notify(@StringRes int message) {
        if (recyclerView != null) {
            Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void notifyWithAction(@StringRes int message, @StringRes int actionTitle,
                                  View.OnClickListener callback) {
        if (recyclerView != null) {
            Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ResourcesCompat.getColor(context.getResources(),
                            R.color.sebastiaan_blue, context.getTheme()))
                    .setAction(actionTitle, callback)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Note that we're clearing downloads onDestroy() but not onRefresh(); the url's generally won't change.
        this.downloads.clear();
    }

    @Override
    public int getItemViewType(int position) {
        TimelineItem item = itemsShowing.get(position);
        return item.type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TimelineItem.TYPE_BULLETIN:
                return new ViewHolder(inflater.inflate(R.layout.view_bulletin_item, parent, false));
            case TimelineItem.TYPE_NEWSLETTER:
                return new ViewHolder(inflater.inflate(R.layout.view_newsletter_item, parent, false));
            default:
                return new ViewHolder(inflater.inflate(R.layout.view_unknown_item, parent, false));
        }
    }

    @Override
    protected void onNext(final TimelineItem item) {
        if (item.type == TimelineItem.TYPE_NEWSLETTER) {
            final String url = item.documentUrl;
            if (!this.downloads.containsKey(url)) {
                final Download download = new Download(url);
                this.downloads.put(url, download);
                if (download.statusCode != Download.STATUS_OPEN_ON_WEB) {
                    subscriptions.add(
                        // 1. Look for the existing item in the DownloadManager.
                        DownloadManagerInterface.getInstance().findExistingEntry(download)
                        // 2. If not found, go get its (uncompressed) file size.
                        .onErrorResumeNext(
                                backendApi.getDownloadSize(download).subscribeOn(Schedulers.io())
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(downloadStatusObserver));
                }
            }
        }
        super.onNext(item);
    }

    private final Observer<Download> downloadStatusObserver = new Observer<Download>() {
        @Override
        public void onCompleted() {
            // Ignored
        }

        @Override
        public void onError(Throwable e) {
            // Ignored
        }

        @Override
        public void onNext(final Download download) {
            Log.d("Timeline", "onNext: " + download);
            downloads.put(download.remoteUrl, download);
            for (int position = 0, max = itemsShowing.size(); position < max; position++) {
                if (download.remoteUrl.equals(itemsShowing.get(position).documentUrl)) {
                    notifyItemChanged(position);
                }
            }
            if (download.statusCode == Download.STATUS_COMPLETED
                    && download.lastStatusCode == Download.STATUS_DOWNLOADING) {
                notifyWithAction(R.string.download_completed, R.string.download_action_open,
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launch(download);
                    }
                });
            }
        }
    };

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimelineItem item = itemsShowing.get(position);
        holder.setItem(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mTitle;
        public final TextView mBody;
        public final TextView mPublishedAt;
        public final DownloadStatusView mDownloadStatus;
        public TimelineItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mBody = (TextView) view.findViewById(R.id.item__body);
            mPublishedAt = (TextView) view.findViewById(R.id.item__published_at);
            mDownloadStatus = (DownloadStatusView) view.findViewById(R.id.item__download_status);
        }

        public void setItem(TimelineItem item) {
            this.mItem = item;
            this.mTitle.setText(item.title);
            this.mPublishedAt.setText(DateUtils.getRelativeDateTimeString(mView.getContext(), item.publishedAt, Period.weeks(1), 0));
            if (item.type == TimelineItem.TYPE_BULLETIN) {
                this.mBody.setText(item.body);
                mView.setOnClickListener(null);
                mView.setClickable(false);
            }
            if (item.type == TimelineItem.TYPE_NEWSLETTER) {
                mView.setOnClickListener(this);
                mView.setClickable(true);
                mDownloadStatus.setStatus(downloads.get(item.documentUrl));
            }
        }

        @Override
        public void onClick(View v) {
            itemsClicked.onNext(mItem);
        }

    }
}
