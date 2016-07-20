package nl.sebastiaanschool.contact.app.gui;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Period;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * RecyclerView adapter for Timeline Items.
 */
class TimelineRVAdapter extends AbstractRVAdapter<TimelineItem, TimelineRVAdapter.ViewHolder> {

    private final PublishSubject<TimelineItem> itemsClicked = PublishSubject.create();

    public TimelineRVAdapter(TimelineRVDataSource timelineDataSource, Listener listener) {
        super(timelineDataSource, listener);
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
        TimelineItem item = items.get(position);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimelineItem item = items.get(position);
        holder.setItem(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mTitle;
        public final TextView mBody;
        public final TextView mPublishedAt;
        public TimelineItem mItem;

        public ViewHolder(View view, @DrawableRes int iconRes) {
            super(view);
            mView = view;
            mView.setOnClickListener(this);
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mBody = (TextView) view.findViewById(R.id.item__body);
            mPublishedAt = (TextView) view.findViewById(R.id.item__published_at);
            GrabBag.applyVectorDrawableLeft(mPublishedAt, iconRes);
        }

        public void setItem(TimelineItem item) {
            this.mItem = item;
            this.mTitle.setText(item.title);
            this.mPublishedAt.setText(DateUtils.getRelativeDateTimeString(mView.getContext(), item.publishedAt, Period.weeks(1), 0));
            if (item.type == TimelineItem.TYPE_BULLETIN) {
                this.mBody.setText(item.body);
            }
        }

        @Override
        public void onClick(View v) {
            itemsClicked.onNext(mItem);
        }

    }
}
