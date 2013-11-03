package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaAdapter extends ArrayAdapter<AgendaItem> {

    private LayoutInflater inflater;

    public AgendaAdapter(Context context) {
        super(context, R.layout.view_agenda_item);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AgendaItem item = getItem(position);
        AgendaItemView view = (AgendaItemView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_agenda_item, parent, false));
        view.setEvent(item);
        return view;
    }
}
