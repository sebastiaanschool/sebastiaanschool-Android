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
public class TeamAdapter extends ArrayAdapter<TeamMember> implements SebListAdapter {

    private LayoutInflater inflater;

    public TeamAdapter(Context context) {
        super(context, R.layout.view_agenda_item);
        this.inflater = LayoutInflater.from(context);
        this.addAll(
            new TeamMember("Jan Klaassen", "Poppenkastpop", "jan.klaassen@example.com"),
            new TeamMember("Katrijn Klaassen", "Poppenkastpop", "katrijn@example.com"));
    }

    public void setDataLoadingCallback(DataLoadingCallback ignored) {
        // We don't do no stinkin' network.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamMember item = getItem(position);
        TeamMemberView view = (TeamMemberView) (convertView != null
                ? convertView
                : inflater.inflate(R.layout.view_team_member, parent, false));
        view.setEvent(item);
        return view;
    }

    @Override
    public void loadData() {
        //Ignored
    }
}
