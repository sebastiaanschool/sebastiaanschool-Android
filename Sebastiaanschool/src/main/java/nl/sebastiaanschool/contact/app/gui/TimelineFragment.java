package nl.sebastiaanschool.contact.app.gui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
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
        return new TimelineRVAdapter(TimelineRVDataSource.getInstance(),
                this, BackendInterface.getInstance(), getContext());
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
