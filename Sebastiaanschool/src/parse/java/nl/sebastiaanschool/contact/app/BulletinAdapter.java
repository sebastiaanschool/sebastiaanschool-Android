/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
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
public class BulletinAdapter extends ParseQueryAdapter<Bulletin> implements SebListAdapter, ParseQueryAdapter.OnQueryLoadListener<Bulletin> {

    private DataLoadingCallback dataLoadingCallback;
    private LayoutInflater inflater;

    public BulletinAdapter(Context context) {
        super(context, new QueryFactory<Bulletin>() {
            @Override
            public ParseQuery<Bulletin> create() {
                ParseQuery<Bulletin> query = new ParseQuery<Bulletin>(Bulletin.class);
                query.orderByDescending("createdAt");
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
    public View getItemView(Bulletin item, View convertView, ViewGroup parent) {
        BulletinItemView view = (BulletinItemView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_bulletin_item, parent, false));
        view.setBulletin(item);
        return view;
    }

    @Override
    public void onLoading() {
        if (dataLoadingCallback != null) {
            dataLoadingCallback.onStartLoading();
        }
    }

    @Override
    public void onLoaded(List<Bulletin> bulletins, Exception e) {
        if (dataLoadingCallback != null) {
            dataLoadingCallback.onStopLoading(e);
        }
    }

    @Override
    public void loadData() {
        this.loadObjects();
    }
}
