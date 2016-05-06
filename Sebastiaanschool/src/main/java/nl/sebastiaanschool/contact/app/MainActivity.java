/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import java.util.List;

import nl.sebastiaanschool.contact.app.gui.NavigationPagerAdapter;
import nl.sebastiaanschool.contact.app.gui.TimelineFragment;

/**
 * Main entry point of our app.
 * <p/>
 * <p>The launch intent is checked for a String extra named {@code com.parse.Channel}. If this is
 * extra is found to contain either of the {@code PushPreferencesUpdater.PUSH_CHANNEL_} constants'
 * values, then the activity automatically navigates to the corresponding screen.</p>
 */
public class MainActivity extends AppCompatActivity {
    private static final IntentFilter DOWNLOAD_COMPLETED_BROADCASTS = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    private NewsletterDownloadHelper newsletterDownloadHelper;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));

        ViewPager vp = (ViewPager) findViewById(R.id.main__content_container);
        vp.setAdapter(new NavigationPagerAdapter(this, getSupportFragmentManager()));

        TabLayout tl = (TabLayout) findViewById(R.id.detail_tabs);
        tl.setupWithViewPager(vp);

        TimelineFragment timelineFragment;
        if (savedInstanceState == null) {
            timelineFragment = new TimelineFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main__content_container, timelineFragment).commit();
        } else {
            timelineFragment = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.main__content_container);
        }
        getApplicationContext().registerReceiver(downloadCompletionReceiver, DOWNLOAD_COMPLETED_BROADCASTS);
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().unregisterReceiver(downloadCompletionReceiver);
        super.onDestroy();
    }

//    @Override
//    public void onItemSelected(int item) {
//        switch (item) {
//            case ITEM_CALL:
//                callSebastiaan();
//                break;
//            case ITEM_HOME:
//                GrabBag.openUri(this, getString(R.string.home_url));
//                break;
//            case ITEM_TWITTER:
//                GrabBag.openUri(this, getString(R.string.twitter_url));
//                break;
//            case ITEM_YURLS:
//                GrabBag.openUri(this, getString(R.string.yurls_url));
//                break;
//        }
//    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem preferences = menu.findItem(R.id.menu_preferences);
        if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
            // Devices with a hardware menu key don't get an overflow button in the action bar for
            // menu items with showAsAction="never". The settings screen becomes very non-obvious.
            // Force the menu item to show as an action on these devices.
            preferences.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Listens for the Download service to tell us one of our downloads finished, then launches the file.
     */
    private final BroadcastReceiver downloadCompletionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (newsletterDownloadHelper == null) {
                newsletterDownloadHelper = new NewsletterDownloadHelper(MainActivity.this);
            }
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                final long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                DownloadManagerAsyncTask newsletterDownloadAsyncTask = new DownloadManagerAsyncTask(MainActivity.this, MainActivity.this.newsletterDownloadHelper);
                newsletterDownloadAsyncTask.execute(new DownloadManagerAsyncTask.Param(downloadId));
            }
        }
    };
}
