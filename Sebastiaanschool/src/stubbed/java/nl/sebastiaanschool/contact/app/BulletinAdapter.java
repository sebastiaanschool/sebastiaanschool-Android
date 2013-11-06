package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by barend on 3-11-13.
 */
public class BulletinAdapter extends ArrayAdapter<Bulletin> implements SebListAdapter {

    private LayoutInflater inflater;

    public BulletinAdapter(Context context) {
        super(context, R.layout.view_agenda_item);
        this.inflater = LayoutInflater.from(context);
        this.addAll(
            new Bulletin("Eerste bulletin", "Dit is een lang verhaal over van alles en nog wat. Het voornaamste is dat het ook op grote telefoons niet op één scherm te vatten is.", 1387756800000L),
            new Bulletin("Tweede bulletin", "Dit is alweer zo'n lang verhaal, maar wel iets minder oeverloos.", 1388707200000L));
    }

    public void setDataLoadingCallback(DataLoadingCallback ignored) {
        // We don't do no stinkin' network.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bulletin item = getItem(position);
        BulletinItemView view = (BulletinItemView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_bulletin_item, parent, false));
        view.setBulletin(item);
        return view;
    }

    @Override
    public void loadData() {
        //Ignored
    }
}
