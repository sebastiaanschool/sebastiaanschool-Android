package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by barend on 6-11-13.
 */
public abstract class SebListFragment<T> extends HorizontalSlidingFragment implements AdapterView.OnItemClickListener {

    private SebListAdapter adapter;
    private DataLoadingCallback dataLoadingCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dataLoadingCallback = (DataLoadingCallback) activity;
        if (this.adapter != null) {
            this.adapter.setDataLoadingCallback(this.dataLoadingCallback);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dataLoadingCallback = null;
        if (this.adapter != null) {
            this.adapter.setDataLoadingCallback(null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = createAdapter(getActivity());
        adapter.setDataLoadingCallback(this.dataLoadingCallback);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        T item = (T) parent.getAdapter().getItem(position);
        onItemClick(item);
    }

    protected abstract SebListAdapter createAdapter(Context context);
    protected abstract void onItemClick(T item);
}