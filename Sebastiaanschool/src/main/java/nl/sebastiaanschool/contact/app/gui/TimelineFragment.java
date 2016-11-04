package nl.sebastiaanschool.contact.app.gui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;
import nl.sebastiaanschool.contact.app.data.server.BackendInterface;

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends AbstractRVFragment<TimelineRVAdapter>
        implements AnalyticsCapableFragment {

    private TimelineRVAdapter adapter;
    private AnalyticsInterface analytics;
    private String analyticsCategory;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    @SuppressWarnings("unused")
    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    protected TimelineRVAdapter createAdapter() {
        adapter = new TimelineRVAdapter(TimelineRVDataSource.getInstance(),
                this, BackendInterface.getInstance(), getContext());
        adapter.enableAnalytics(analytics, analyticsCategory);
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
                // Suppress itemChanged events, these fire when download state changes and we don't
                // want the list item to blink if that happens.
                dispatchChangeFinished(oldHolder, true);
                if (newHolder != null && newHolder != oldHolder) {
                    dispatchChangeFinished(newHolder, false);
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void enableAnalytics(AnalyticsInterface analytics, String category) {
        this.analytics = analytics;
        this.analyticsCategory = category;
        if (adapter != null) {
            adapter.enableAnalytics(analytics, category);
        }
    }

    public void refreshAndScrollToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
            if (!adapter.isRefreshing()) {
                this.onRefresh();
            }
        }
    }

    public void onTimelineUpdateBroadcastReceived() {
        if (recyclerView != null) {
            boolean firstItemNotVisible = 0 < recyclerView.getChildAdapterPosition(
                    recyclerView.getChildAt(0));
            if (!adapter.isRefreshing()) {
                adapter.refresh();
            }
            if (firstItemNotVisible) {
                // We're not scrolled to the top of the list, show a snackbar
                final Snackbar sb = Snackbar.make(recyclerView,
                        R.string.toast__new_timeline_items_timeline_visible, Snackbar.LENGTH_LONG);
                sb.setAction(R.string.toast__scroll_to_top, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.smoothScrollToPosition(0);
                        sb.dismiss();
                    }
                });
                sb.show();
            }
        }
    }
}
