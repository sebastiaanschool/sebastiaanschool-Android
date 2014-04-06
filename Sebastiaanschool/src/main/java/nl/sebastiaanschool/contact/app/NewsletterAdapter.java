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
public class NewsletterAdapter extends ParseQueryAdapter<Newsletter> implements SebListAdapter, ParseQueryAdapter.OnQueryLoadListener<Newsletter> {

    private DataLoadingCallback dataLoadingCallback;
    private LayoutInflater inflater;

    public NewsletterAdapter(Context context) {
        super(context, new QueryFactory<Newsletter>() {
            @Override
            public ParseQuery<Newsletter> create() {
                ParseQuery<Newsletter> query = new ParseQuery<>(Newsletter.class);
                query.orderByDescending("publishedAt");
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
    public View getItemView(Newsletter item, View convertView, ViewGroup parent) {
        NewsletterView view = (NewsletterView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_newsletter_item, parent, false));
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
    public void onLoaded(List<Newsletter> newsletters, Exception e) {
        if (dataLoadingCallback != null) {
            dataLoadingCallback.onStopLoading(e);
        }
    }
}
