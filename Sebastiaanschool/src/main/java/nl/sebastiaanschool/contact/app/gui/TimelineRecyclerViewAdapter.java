package nl.sebastiaanschool.contact.app.gui;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */
class TimelineRecyclerViewAdapter extends AbstractRVFragment.DestroyableRecyclerViewAdapter<TimelineRecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_UNKNOWN = 0;
    private static final int TYPE_BULLETIN = 1;
    private static final int TYPE_NEWSLETTER = 2;

    private final List<TimelineItem> mValues;
    private CompositeSubscription subscriptions = new CompositeSubscription();


    public TimelineRecyclerViewAdapter(TimelineDataSource timelineDataSource) {
        mValues = new ArrayList<>();
        subscriptions.add(timelineDataSource.getTimeline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimelineItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TimelineAdapter", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TimelineAdapter", "onError");
                    }

                    @Override
                    public void onNext(List<TimelineItem> timelineItem) {
                        Log.d("TimelineAdapter", "onNext - " + timelineItem.size());
                        // TODO this is a bit blunt; I'm also unsure if the adapter should handle subscription.
                        mValues.clear();
                        mValues.addAll(timelineItem);
                        notifyDataSetChanged();
                    }
                }));
    }

    @Override
    public int getItemViewType(int position) {
        TimelineItem item = mValues.get(position);
        if (item instanceof TimelineItem.Bulletin) {
            return TYPE_BULLETIN;
        } else if (item instanceof TimelineItem.Newsletter) {
            return TYPE_NEWSLETTER;
        } else {
            return TYPE_UNKNOWN;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_BULLETIN:
                return new ViewHolder(inflater.inflate(R.layout.view_bulletin_item, parent, false),
                        R.drawable.ic_timeline_bulletin_24dp);
            case TYPE_NEWSLETTER:
                return new ViewHolder(inflater.inflate(R.layout.view_newsletter_item, parent, false),
                        R.drawable.ic_timeline_newsletter_24dp);
            default:
                return new ViewHolder(inflater.inflate(R.layout.view_unknown_item, parent, false),
                        R.drawable.ic_timeline_unknown_24dp);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimelineItem item = mValues.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void onDestroy() {
        subscriptions.unsubscribe();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mBody;
        public final TextView mPublishedAt;
        public TimelineItem mItem;

        public ViewHolder(View view, @DrawableRes int iconRes) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mBody = (TextView) view.findViewById(R.id.item__body);
            mPublishedAt = (TextView) view.findViewById(R.id.item__published_at);
            GrabBag.applyVectorDrawableLeft(mPublishedAt, iconRes);
        }

        public void setItem(TimelineItem item) {
            this.mItem = item;
            this.mTitle.setText(item.title);
            this.mPublishedAt.setText(DateUtils.getRelativeDateTimeString(mView.getContext(), item.publishedAt, Period.weeks(1), 0));
            if (item instanceof TimelineItem.Bulletin) {
                TimelineItem.Bulletin bulletin = (TimelineItem.Bulletin) item;
                this.mBody.setText(bulletin.body);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
