package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 */
public abstract class AbstractRVFragment<T extends AbstractRVFragment.DestroyableRecyclerViewAdapter> extends Fragment {

    private T adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AbstractRVFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = createAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        final RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CardMarginsDecorator(context));
        return recyclerView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
    }

    protected T getAdapter() {
        return adapter;
    }

    protected abstract T createAdapter();

    public static abstract class DestroyableRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<VH> {
        protected abstract void onDestroy();
    }
}
