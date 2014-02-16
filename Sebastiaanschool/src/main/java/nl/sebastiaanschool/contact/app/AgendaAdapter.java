/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaAdapter extends ParseQueryAdapter<AgendaItem> implements SebListAdapter, ParseQueryAdapter.OnQueryLoadListener<AgendaItem> {

    private DataLoadingCallback dataLoadingCallback;
    private LayoutInflater inflater;

    public AgendaAdapter(Context context) {
        super(context, new QueryFactory<AgendaItem>() {
            @Override
            public ParseQuery<AgendaItem> create() {
                ParseQuery<AgendaItem> query = new ParseQuery<>(AgendaItem.class);
                query.addAscendingOrder("start");
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                return query;
            }
        });
        this.setAutoload(true);
        this.addOnQueryLoadListener(this);
        this.inflater = LayoutInflater.from(context);
    }

    public void setDataLoadingCallback(DataLoadingCallback dataLoadingCallback) {
        this.dataLoadingCallback = dataLoadingCallback;
    }

    @Override
    public View getItemView(AgendaItem item, View convertView, ViewGroup parent) {
        AgendaItemView view = (AgendaItemView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_agenda_item, parent, false));
        view.setEvent(item);
        return view;
    }

    @Override
    public void onLoading() {
        if (dataLoadingCallback != null) {
            dataLoadingCallback.onStartLoading();
        }
    }

    @Override
    public void onLoaded(List<AgendaItem> agendaItems, Exception e) {
        if (dataLoadingCallback != null) {
            dataLoadingCallback.onStopLoading(e);
        }
    }

    @Override
    public void loadData() {
        this.loadObjects();
    }
}
