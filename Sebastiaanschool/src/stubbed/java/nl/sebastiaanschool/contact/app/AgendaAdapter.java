/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaAdapter extends ArrayAdapter<AgendaItem> implements SebListAdapter {

    private LayoutInflater inflater;

    public AgendaAdapter(Context context) {
        super(context, R.layout.view_agenda_item);
        this.inflater = LayoutInflater.from(context);
        this.addAll(
            new AgendaItem("Kerstvakantie 2013", 1387756800000L, 1388707200000L),
            new AgendaItem("Voorjaarsvakantie 2014", 1392595200000L, 1392940800000L),
            new AgendaItem("Goede Vrijdag 2014", 1397779200000L));
    }

    public void setDataLoadingCallback(DataLoadingCallback ignored) {
        // We don't do no stinkin' network.
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

    @Override
    public void loadData() {
        //Ignored
    }
}
