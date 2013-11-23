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

import nl.sebastiaanschool.contact.app.Newsletter;

/**
 * Created by barend on 3-11-13.
 */
public class NewsletterAdapter extends ArrayAdapter<Newsletter> implements SebListAdapter {

    private LayoutInflater inflater;

    public NewsletterAdapter(Context context) {
        super(context, R.layout.view_agenda_item);
        this.inflater = LayoutInflater.from(context);
        this.addAll(
            new Newsletter("PDF URL (404)", "https://github.com/barend/sebastiaanschool/bestaatniet.pdf", 1387756800000L),
            new Newsletter("Non-PDF URL", "https://github.com/barend/sebastiaanschool", 1388707200000L),
            new Newsletter("PDF URL (2.2MB)", "http://downloads.bbc.co.uk/schools/teachers/shakespeare_unlocked/mnd.pdf", 1388706400000L),
            new Newsletter("Invalid PDF URL (unknown scheme)", "jvooi453hfsafdj://example.com/failure.pdf", 1388706400000L),
            new Newsletter("Invalid Non-PDF URL (unknown scheme)", "jvooi453hfsafdj://example.com/failure.txt", 1388705200000L)
        );
    }

    public void setDataLoadingCallback(DataLoadingCallback ignored) {
        // We don't do no stinkin' network.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Newsletter item = getItem(position);
        NewsletterView view = (NewsletterView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_newsletter_item, parent, false));
        view.setEvent(item);
        return view;
    }

    @Override
    public void loadData() {
        //Ignored
    }
}
