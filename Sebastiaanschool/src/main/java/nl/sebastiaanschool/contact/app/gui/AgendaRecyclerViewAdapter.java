package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
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
import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * RecyclerView adapter for Agenda Items.
 */
class AgendaRecyclerViewAdapter extends AbstractRVFragment.DestroyableRecyclerViewAdapter<AgendaRecyclerViewAdapter.ViewHolder> {

    private final List<AgendaItem> mValues;
    private CompositeSubscription subscriptions = new CompositeSubscription();


    public AgendaRecyclerViewAdapter(AgendaDataSource agendaDataSource) {
        mValues = new ArrayList<>();
        subscriptions.add(agendaDataSource.getAgenda()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AgendaItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TimelineAdapter", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TimelineAdapter", "onError");
                    }

                    @Override
                    public void onNext(List<AgendaItem> agendaItem) {
                        Log.d("TimelineAdapter", "onNext - " + agendaItem.size());
                        // TODO this is a bit blunt; I'm also unsure if the adapter should handle subscription.
                        mValues.clear();
                        mValues.addAll(agendaItem);
                        notifyDataSetChanged();
                    }
                }));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_agenda_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AgendaItem item = mValues.get(position);
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
        public static final int DATE_FLAGS = DateUtils.FORMAT_SHOW_DATE
                                           | DateUtils.FORMAT_SHOW_TIME
                                           | DateUtils.FORMAT_SHOW_WEEKDAY
                                           | DateUtils.FORMAT_ABBREV_ALL;
        public final View mView;
        public final TextView mTitle;
        public final TextView mDateRange;
        public AgendaItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mDateRange = (TextView) view.findViewById(R.id.item__date_range);
            GrabBag.applyVectorDrawableLeft(mDateRange, R.drawable.ic_agenda_event_24dp);
        }

        public void setItem(AgendaItem item) {
            this.mItem = item;
            this.mTitle.setText(item.title);

            final Context ctx = this.mDateRange.getContext();
            this.mDateRange.setText(item.end == null
                    ? DateUtils.getRelativeDateTimeString(ctx, item.start, Period.weeks(1), DATE_FLAGS)
                    : DateUtils.formatDateRange(ctx, item.start, item.end, DATE_FLAGS));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
