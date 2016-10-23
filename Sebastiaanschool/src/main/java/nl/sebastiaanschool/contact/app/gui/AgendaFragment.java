package nl.sebastiaanschool.contact.app.gui;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;
import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;

public class AgendaFragment extends AbstractRVFragment<AgendaRVAdapter>
        implements AnalyticsCapableFragment {

    private final SubscriptionList subscriptions = new SubscriptionList();
    private AnalyticsInterface analytics;
    private String analyticsCategory;

    public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance() {
        return new AgendaFragment();
    }

    @Override
    protected AgendaRVAdapter createAdapter() {
        final AgendaRVAdapter adapter = new AgendaRVAdapter(AgendaRVDataSource.getInstance(), this);
        this.subscriptions.add(adapter.itemsClicked().subscribe(new Action1<AgendaItem>() {
            @Override
            public void call(AgendaItem item) {
                publishCalendarEvent(item);
            }
        }));
        return adapter;
    }

    public void enableAnalytics(AnalyticsInterface analytics, String category) {
        this.analytics = analytics;
        this.analyticsCategory = category;
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
            if (analytics != null) {
                analytics.itemSelected(analyticsCategory, item.url, item.title);
            }
            getActivity().startActivity(intent);
        } catch (Exception e) {
            // Fail silently.
            FirebaseCrash.logcat(Log.DEBUG, "AF", "No handler for calendar event. " + e.toString());
        }
    }
}
