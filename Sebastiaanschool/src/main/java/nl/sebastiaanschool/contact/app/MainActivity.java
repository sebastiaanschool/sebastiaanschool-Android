package nl.sebastiaanschool.contact.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity implements NavigationFragment.Callback, FragmentManager.OnBackStackChangedListener {

    private boolean detailFragmentVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
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
        ActionBar actionBar = getActionBar();
        // TODO don't present up navigation until after the detail fragment finished its entry animation.
        // TODO detach the navigation fragment when the sliding animation completes.
        actionBar.setHomeButtonEnabled(detailFragmentVisible);
        actionBar.setDisplayHomeAsUpEnabled(detailFragmentVisible);
    }
}
