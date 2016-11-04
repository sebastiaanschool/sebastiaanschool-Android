package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxrelay.PublishRelay;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Period;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import rx.Observable;

/**
 * RecyclerView adapter for Agenda Items.
 */
class AgendaRVAdapter extends AbstractRVAdapter<AgendaItem, AgendaRVAdapter.ViewHolder> {

    private final PublishRelay<AgendaItem> itemsClicked = PublishRelay.create();

    public AgendaRVAdapter(AgendaRVDataSource agendaDataSource, Listener listener) {
        super(agendaDataSource, listener);
    }

    /**
     * A hot observable that emits items that have been tapped/clicked by the operator.
     * @return an observable.
     */
    public Observable<AgendaItem> itemsClicked() {
        return itemsClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_agenda_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AgendaItem item = itemsShowing.get(position);
        holder.setItem(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mView.setOnClickListener(this);
            mTitle = (TextView) view.findViewById(R.id.item__title);
            mDateRange = (TextView) view.findViewById(R.id.item__date_range);
            ImageView icon = (ImageView) view.findViewById(R.id.item__picture);
            GrabBag.applyVectorImage(icon, R.drawable.ic_agenda_event_24dp);
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
        public void onClick(View v) {
            itemsClicked.call(mItem);
        }
    }
}
