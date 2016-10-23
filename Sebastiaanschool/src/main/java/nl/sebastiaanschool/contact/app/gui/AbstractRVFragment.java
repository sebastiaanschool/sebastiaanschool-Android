package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import nl.sebastiaanschool.contact.app.R;

/**
 * A fragment representing a list of Items.
 */
abstract class AbstractRVFragment<A extends AbstractRVAdapter> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, AbstractRVAdapter.Listener {

    private A adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    private int refreshCount;

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
        recyclerView = new RecyclerView(context);
        recyclerView.setId(R.id.gui__recycler_view);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CardMarginsDecorator(context));
        int top = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_top_first);
        int bottom = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_bottom_last);
        recyclerView.setPadding(0, top, 0, bottom);
        recyclerView.setClipToPadding(false);

        swipeRefreshLayout.addView(recyclerView);
        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            // This reference could be null if you rotate multiple times in quick succession.
            adapter.onDestroy();
        }
    }

    @Override
    public void onRefresh() {
        if (adapter.isRefreshing()) {
            if (++refreshCount > 3) {
                Toast.makeText(getContext(), R.string.refresh_in_progress, Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            refreshCount = 0;
            adapter.refresh();
        }
    }

    @Override
    public void startedLoadingData() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void finishedLoadingData() {
        swipeRefreshLayout.setRefreshing(false);
    }

    protected abstract A createAdapter();
}
