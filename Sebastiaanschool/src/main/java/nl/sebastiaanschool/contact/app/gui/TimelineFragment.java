package nl.sebastiaanschool.contact.app.gui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
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
        final TimelineRVAdapter adapter = new TimelineRVAdapter(TimelineRVDataSource.getInstance(), this, BackendInterface.getInstance());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder,
                                         RecyclerView.ViewHolder newHolder,
                                         int fromX, int fromY, int toX, int toY) {
                dispatchChangeFinished(oldHolder, true);
                if (newHolder != null && newHolder != oldHolder) {
                    dispatchChangeFinished(newHolder, false);
                }
                return false;
            }
        });
        return view;
    }
}
