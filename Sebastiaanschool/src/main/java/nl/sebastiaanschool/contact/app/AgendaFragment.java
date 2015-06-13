/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Barend on 1-11-13.
 */
public class AgendaFragment extends SebListFragment<AgendaItem> {

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new AgendaAdapter(context);
    }

    @Override
    protected void onItemClick(AgendaItem item) {
        publishCalendarEvent(item);
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__agenda;
    }

    @Override
    public int getAnnouncementResId() {
        return R.string.accessibility__announce_open_agenda;
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
