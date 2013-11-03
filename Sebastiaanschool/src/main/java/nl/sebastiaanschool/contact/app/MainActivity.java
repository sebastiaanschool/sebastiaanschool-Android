package nl.sebastiaanschool.contact.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import java.util.List;

public class MainActivity extends Activity implements NavigationFragment.Callback, FragmentManager.OnBackStackChangedListener, HorizontalSlidingFragment.Callback {

    private boolean detailFragmentVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setIcon(R.drawable.ic_sebastiaan_48dp_white);
        setContentView(R.layout.activity_main);
        getFragmentManager().addOnBackStackChangedListener(this);
        getFragmentManager().beginTransaction().add(R.id.main__content_container, new NavigationFragment()).commit();
    }

    @Override
    public void onItemSelected(int item) {
        switch (item) {
            case ITEM_AGENDA:
                pushFragment(new AgendaFragment(), "Agenda");
                break;
            case ITEM_CALL:
                callSebastiaan();
                break;
            case ITEM_HOME:
                openUrl(getString(R.string.home_url));
                break;
            case ITEM_TWITTER:
                openUrl(getString(R.string.twitter_url));
                break;
            case ITEM_YURLS:
                openUrl(getString(R.string.yurls_url));
                break;
        }
    }

    private void callSebastiaan() {
        final String number = getResources().getString(R.string.call_url);
        final Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> handlers = getPackageManager().queryIntentActivities(dial, 0);
        boolean fail = handlers.isEmpty();
        if (!fail) {
            try {
                startActivity(dial);
            } catch (Exception e) {
                fail = true;
            }
        }
        if (fail) {
            // Unlikely to occur. Tablets generally register their contacts app to handle tel: URI's.
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage(R.string.call_failed_dialog_body)
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    private void openUrl(String uri) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Need to check if a browser is present, as it can be disabled entirely using child safety features on a tablet.
        List<ResolveInfo> handlers = getPackageManager().queryIntentActivities(browse, 0);
        if (!handlers.isEmpty()) {
            startActivity(browse);
        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage(uri)
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    private void pushFragment(HorizontalSlidingFragment fragment, String label) {
        if (detailFragmentVisible)
            return;
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        fragment.addWithAnimation(tx, R.id.main__content_container, label);
        tx.commit();
    }

    private void popFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            popFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        detailFragmentVisible = getFragmentManager().getBackStackEntryCount() > 0;
    }

    @Override
    public void onSlidingFragmentBeginAnimation(HorizontalSlidingFragment source, boolean willOpen) {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        // TODO optimize GPU overdraw: -> toggle navigationFragment's view visibility before/after sliding animation.
    }

    @Override
    public void onSlidingFragmentEndAnimation(HorizontalSlidingFragment source, boolean hasOpened) {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(hasOpened);
        actionBar.setDisplayHomeAsUpEnabled(hasOpened);
    }
}
