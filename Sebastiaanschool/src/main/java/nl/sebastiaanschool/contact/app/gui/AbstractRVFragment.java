package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.R;

/**
 * A fragment representing a list of Items.
 */
abstract class AbstractRVFragment<A extends AbstractRVAdapter> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, AbstractRVAdapter.Listener {

    private A adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AbstractRVFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = inflater.getContext();

        swipeRefreshLayout = new SwipeRefreshLayout(context);
        swipeRefreshLayout.setId(R.id.gui__swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        adapter = createAdapter();
        final RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setId(R.id.gui__recycler_view);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CardMarginsDecorator(context));

        swipeRefreshLayout.addView(recyclerView);
        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
    }

    @Override
    public void onRefresh() {
        adapter.refresh();
    }

    @Override
    public void startedLoadingData() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void finishedLoadingData(boolean successfully) {
        swipeRefreshLayout.setRefreshing(false);
        if (!successfully) {
            // TODO Fix Bug: snackbar is glitchy if server is down.
            // The snackbar attaches to the CoordinatorLayout, of which one is shared for all tabs. This means that if the
            // app has no access to the server, multiple snackbars fall push eachother out of the way and they also on top
            // fragments that have no network function.
            final boolean empty = adapter.getItemCount() == 0;
            final int messageRes = empty ? R.string.data_loading_failed : R.string.data_refreshing_failed;
            final int duration   = empty ? Snackbar.LENGTH_INDEFINITE   : Snackbar.LENGTH_SHORT;

            final Snackbar snackbar = Snackbar.make(swipeRefreshLayout, messageRes, duration);
            if (empty) {
                snackbar.setAction(R.string.data_loading_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.refresh();
                    }
                });
            }
            snackbar.show();
        }
    }

    protected A getAdapter() {
        return adapter;
    }

    protected abstract A createAdapter();
}
