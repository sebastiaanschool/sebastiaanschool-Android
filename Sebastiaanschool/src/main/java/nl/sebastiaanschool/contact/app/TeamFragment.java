package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

    private void composeEmail(TeamMember item) {
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
