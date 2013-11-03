package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Barend on 1-11-13.
 */
public class AgendaFragment extends HorizontalSlidingFragment implements AdapterView.OnItemClickListener {

    private DataLoadingCallback dataLoadingCallback;
    private AgendaAdapter adapter;

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
    public View onCreateView2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new AgendaAdapter(getActivity());
        adapter.setDataLoadingCallback(this.dataLoadingCallback);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AgendaItem item = (AgendaItem) parent.getAdapter().getItem(position);
        publishCalendarEvent(item);
    }

    private void publishCalendarEvent(AgendaItem item) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", item.getTitle());
        intent.putExtra("allDay", true);
        intent.putExtra("beginTime", item.getStartTimestamp());
        if (item.hasEndDate()) {
            intent.putExtra("endTime", item.getEndTimestamp());
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            // Fail silently.
            android.util.Log.e("AgendaFragment", "Failed to ACTION_EDIT a vnd.android.cursor.item/event", e);
        }
    }
}
