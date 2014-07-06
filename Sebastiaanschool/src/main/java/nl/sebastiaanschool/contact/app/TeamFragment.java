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
import android.net.Uri;

import com.parse.ParseAnalytics;

/**
 * Created by Barend on 1-11-13.
 */
public class TeamFragment extends SebListFragment<TeamMember> {

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new TeamAdapter(context);
    }

    @Override
    protected void onItemClick(TeamMember item) {
        composeEmail(item);
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__team;
    }

    @Override
    public int getAnnouncementResId() {
        return R.string.accessibility__announce_open_team;
    }

    private void composeEmail(TeamMember item) {
        ParseAnalytics.trackEvent("Navigate to email client");
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", item.getEmail(), null));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.team_email_body, item.getDisplayName()));
        try {
            startActivity(intent);
        } catch (Exception e) {
            // Fail silently.
            android.util.Log.e("TeamFragment", "Failed to ACTION_SEND an email", e);
        }
    }
}
