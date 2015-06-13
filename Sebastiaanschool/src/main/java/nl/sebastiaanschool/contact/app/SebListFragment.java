/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by barend on 6-11-13.
 */
public abstract class SebListFragment<T> extends SebFragment implements AdapterView.OnItemClickListener {

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
