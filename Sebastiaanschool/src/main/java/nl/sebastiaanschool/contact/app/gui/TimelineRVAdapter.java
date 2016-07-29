package nl.sebastiaanschool.contact.app.gui;

import android.support.annotation.DrawableRes;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Period;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.downloadmanager.Download;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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

    public TimelineRVAdapter(TimelineRVDataSource timelineDataSource, Listener listener,
                             BackendInterface backendApi) {
        super(timelineDataSource, listener);
        this.backendApi = backendApi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Note that we're clearing downloads onDestroy() but not onRefresh(); the url's generally won't change.
        this.downloads.clear();
    }

    /**
     * A hot observable that emits items that have been tapped/clicked by the operator.
     * @return an observable.
     */
    public Observable<TimelineItem> itemsClicked() {
        return itemsClicked;
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
                return new ViewHolder(inflater.inflate(R.layout.view_bulletin_item, parent, false),
                        R.drawable.ic_timeline_bulletin_24dp);
            case TimelineItem.TYPE_NEWSLETTER:
                return new ViewHolder(inflater.inflate(R.layout.view_newsletter_item, parent, false),
                        R.drawable.ic_timeline_newsletter_24dp);
            default:
                return new ViewHolder(inflater.inflate(R.layout.view_unknown_item, parent, false),
                        R.drawable.ic_timeline_unknown_24dp);
        }
    }

    @Override
    protected void onNext(final TimelineItem item) {
        if (item.type == TimelineItem.TYPE_NEWSLETTER) {
            final String url = item.documentUrl;
            if (!this.downloads.containsKey(url)) {
                final Download download = new Download(url);
                this.downloads.put(url, download);
                // TODO Locate item in DownloadManager
                // TODO If found - update status
                // (else) if not found - obtain download size
                subscriptions.add(backendApi.getDownloadSize(download)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(downloadStatusObserver));
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
        public void onNext(Download download) {
            downloads.put(download.url, download);
            for (int position = 0, max = itemsShowing.size(); position < max; position++) {
                if (download.url.equals(itemsShowing.get(position).documentUrl)) {
                    notifyItemChanged(position);
                }
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

        public ViewHolder(View view, @DrawableRes int iconRes) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mBody = (TextView) view.findViewById(R.id.item__body);
            mPublishedAt = (TextView) view.findViewById(R.id.item__published_at);
            mDownloadStatus = (DownloadStatusView) view.findViewById(R.id.item__download_status);
            GrabBag.applyVectorDrawableLeft(mPublishedAt, iconRes);
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
