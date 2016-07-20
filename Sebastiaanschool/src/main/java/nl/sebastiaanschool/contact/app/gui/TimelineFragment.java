package nl.sebastiaanschool.contact.app.gui;

import android.util.Log;

import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.SubscriptionList;

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends AbstractRVFragment<TimelineRVAdapter> {

    private SubscriptionList subscriptions = new SubscriptionList();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    @SuppressWarnings("unused")
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    @Override
    protected TimelineRVAdapter createAdapter() {
        final TimelineRVAdapter adapter = new TimelineRVAdapter(TimelineRVDataSource.getInstance(), this);
        subscriptions.add(adapter.itemsClicked()
                .filter(new Func1<TimelineItem, Boolean>() {
                    @Override
                    public Boolean call(TimelineItem timelineItem) {
                        return timelineItem.type == TimelineItem.TYPE_NEWSLETTER;
                    }
                })
                .subscribe(new Action1<TimelineItem>() {
                    @Override
                    public void call(TimelineItem newsletter) {
                        Log.i("Timeline", "Newsletter clicked: " + newsletter.documentUrl);
                        GrabBag.openUri(getContext(), newsletter.documentUrl);
                    }
                }));
        return adapter;
    }
}
