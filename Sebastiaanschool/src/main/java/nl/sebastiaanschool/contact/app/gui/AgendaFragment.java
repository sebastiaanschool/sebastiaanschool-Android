package nl.sebastiaanschool.contact.app.gui;


import android.content.Intent;

import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;

public class AgendaFragment extends AbstractRVFragment<AgendaRecyclerViewAdapter> {

    private SubscriptionList subscriptions = new SubscriptionList();

    public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance() {
        return new AgendaFragment();
    }

    @Override
    protected AgendaRecyclerViewAdapter createAdapter() {
        final AgendaRecyclerViewAdapter adapter = new AgendaRecyclerViewAdapter(AgendaRVDataSource.getInstance(), this);
        this.subscriptions.add(adapter.itemsClicked().subscribe(new Action1<AgendaItem>() {
            @Override
            public void call(AgendaItem item) {
                publishCalendarEvent(item);
            }
        }));
        return adapter;
    }

    @Override
    public void onDestroy() {
        subscriptions.unsubscribe();
        super.onDestroy();
    }

    private void publishCalendarEvent(AgendaItem item) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", item.title);
        intent.putExtra("allDay", true);
        intent.putExtra("beginTime", item.start.getMillis());
        if (item.end != null) {
            intent.putExtra("endTime", item.end.getMillis());
        }
        try {
            getActivity().startActivity(intent);
        } catch (Exception e) {
            // Fail silently.
            android.util.Log.e("AgendaFragment", "Failed to ACTION_EDIT a vnd.android.cursor.item/event", e);
        }
    }
}
