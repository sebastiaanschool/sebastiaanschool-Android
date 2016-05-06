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

import nl.sebastiaanschool.contact.app.R;

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends Fragment {

    private TimelineRecyclerViewAdapter timelineAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    @SuppressWarnings("unused")
    public static TimelineFragment newInstance(int columnCount) {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timelineAdapter = new TimelineRecyclerViewAdapter(TimelineDataSource.getInstance(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_timeline_list, container, false);
        final Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(timelineAdapter);
        recyclerView.addItemDecoration(new CardMarginsDecorator(context));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timelineAdapter.onDestroy();
    }
}
