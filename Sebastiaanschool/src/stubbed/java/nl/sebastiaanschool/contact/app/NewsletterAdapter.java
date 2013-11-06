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
            new Newsletter("Eerste nieuwsbrief", "https://github.com/jeroenleenarts/sebastiaanschool", 1387756800000L),
            new Newsletter("Tweede nieuwsbrief", "https://github.com/barend/sebastiaanschool", 1388707200000L));
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
