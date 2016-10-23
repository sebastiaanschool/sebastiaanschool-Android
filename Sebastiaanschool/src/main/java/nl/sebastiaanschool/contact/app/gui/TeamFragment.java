package nl.sebastiaanschool.contact.app.gui;


import android.content.Intent;
import android.net.Uri;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;

import static nl.sebastiaanschool.contact.app.gui.GrabBag.assertOnMainThread;

public class TeamFragment extends AbstractRVFragment<TeamRVAdapter> {

    private final SubscriptionList subscriptions = new SubscriptionList();

    public TeamFragment() {
        // Required empty public constructor
    }

    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    @Override
    protected TeamRVAdapter createAdapter() {
        final TeamRVAdapter adapter = new TeamRVAdapter(TeamRVDataSource.getInstance(), this);
        subscriptions.add(adapter.itemsClicked().subscribe(new Action1<TeamItem>() {
            @Override
            public void call(TeamItem teamItem) {
                composeEmail(teamItem);
            }
        }));
        return adapter;
    }

    private void composeEmail(TeamItem item) {
        assertOnMainThread();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", item.email, null));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.team_email_body, item.displayName));
        try {
            // Don't send an analytics event - tracking un-sent emails is a bit
            // closer than we want to get.
            startActivity(intent);
        } catch (Exception e) {
            // Fail silently.
            android.util.Log.e("TeamFragment", "Failed to ACTION_SEND an email", e);
        }
    }
}
