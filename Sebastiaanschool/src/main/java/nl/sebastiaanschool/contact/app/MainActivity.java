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

public class MainActivity extends Activity implements NavigationFragment.Callback, FragmentManager.OnBackStackChangedListener, HorizontalSlidingFragment.Callback, DataLoadingCallback {

    private String NAVIGATION_FRAGMENT_TAG = "navFrag";
    private NavigationFragment navigationFragment;
    private boolean detailFragmentVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getActionBar().setIcon(R.drawable.ic_sebastiaan_48dp_white);
        setContentView(R.layout.activity_main);
        getFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            navigationFragment = new NavigationFragment();
            getFragmentManager().beginTransaction().add(R.id.main__content_container, navigationFragment, NAVIGATION_FRAGMENT_TAG).commit();
        } else {
            navigationFragment = (NavigationFragment) getFragmentManager().findFragmentByTag(NAVIGATION_FRAGMENT_TAG);
        }
        Analytics.trackAppOpened(getIntent());
    }

    @Override
    public void onItemSelected(int item) {
        switch (item) {
            case ITEM_AGENDA:
                pushFragment(new AgendaFragment(), getString(R.string.navigation__agenda));
                break;
            case ITEM_BULLETIN:
                pushFragment(new BulletinFragment(), getString(R.string.navigation__bulletin));
                break;
            case ITEM_CALL:
                callSebastiaan();
                break;
            case ITEM_HOME:
                GrabBag.openUri(this, getString(R.string.home_url));
                break;
            case ITEM_NEWSLETTER:
                pushFragment(new NewsletterFragment(), getString(R.string.navigation__newsletter));
                break;
            case ITEM_TEAM:
                pushFragment(new TeamFragment(), getString(R.string.navigation__team));
                break;
            case ITEM_TWITTER:
                GrabBag.openUri(this, getString(R.string.twitter_url));
                break;
            case ITEM_YURLS:
                GrabBag.openUri(this, getString(R.string.yurls_url));
                break;
        }
    }

    private void callSebastiaan() {
        Analytics.trackEvent("Navigate to dialer");
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

    private void pushFragment(HorizontalSlidingFragment fragment, String label) {
        if (detailFragmentVisible)
            return;
        detailFragmentVisible = true;
        Analytics.trackEvent("Navigate to " + label);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        fragment.addWithAnimation(tx, R.id.main__content_container, label);
        tx.commit();
    }

    private void popFragment() {
        Analytics.trackEvent("Navigate to home");
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
    public void onSlidingFragmentBeginAnimation(HorizontalSlidingFragment source, boolean willSlideIntoView) {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        if (!willSlideIntoView) {
            // Before the detail fragment begins moving out of screen, make the underlying navigation fragment visible.
            navigationFragment.setVisible(true);
        }
    }

    @Override
    public void onSlidingFragmentEndAnimation(HorizontalSlidingFragment source, boolean didSlideIntoView) {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(didSlideIntoView);
        actionBar.setDisplayHomeAsUpEnabled(didSlideIntoView);
        if (didSlideIntoView) {
            // After the detail fragment has appeared on top of the navigation fragment, hide the latter to reduce GPU overdraw.
            navigationFragment.setVisible(false);
        }
    }

    @Override
    public void onStartLoading() {
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onStopLoading(Exception e) {
        // TODO report exceptions to analytics
        this.setProgressBarIndeterminateVisibility(false);
    }
}
