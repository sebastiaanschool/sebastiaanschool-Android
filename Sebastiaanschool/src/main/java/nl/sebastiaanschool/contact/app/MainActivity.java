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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Main entry point of our app.
 * <p/>
 * <p>The launch intent is checked for a String extra named {@code com.parse.Channel}. If this is
 * extra is found to contain either of the {@code PushPreferencesUpdater.PUSH_CHANNEL_} constants'
 * values, then the activity automatically navigates to the corresponding screen.</p>
 */
public class MainActivity extends AppCompatActivity implements NavigationFragment.Callback, DataLoadingCallback, Handler.Callback, NewsletterFragment.Callback {
    private static final IntentFilter DOWNLOAD_COMPLETED_BROADCASTS = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    private static final int PAGE_NEWSLETTER = 1;
    private static final int PAGE_BULLETIN = 2;
    private static final int MESSAGE_OPEN_PAGE = 1;
    private static final String NAVIGATION_FRAGMENT_TAG = "navFrag";
    private static final long PAGE_OPEN_DELAY = 250L;
    private Handler messageHandler = new Handler(Looper.getMainLooper(), this);
    private NavigationFragment navigationFragment;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout toolbarLayout;
    private NewsletterDownloadHelper newsletterDownloadHelper;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main__toolbar_container);
        toolbarLayout.setTitle(getTitle());
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
        if (savedInstanceState == null) {
            navigationFragment = new NavigationFragment();
            getFragmentManager().beginTransaction().add(R.id.main__content_container, navigationFragment, NAVIGATION_FRAGMENT_TAG).commit();
        } else {
            navigationFragment = (NavigationFragment) getFragmentManager().findFragmentByTag(NAVIGATION_FRAGMENT_TAG);
        }
        getApplicationContext().registerReceiver(downloadCompletionReceiver, DOWNLOAD_COMPLETED_BROADCASTS);
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().unregisterReceiver(downloadCompletionReceiver);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        autoNavigateToDetailPageIfNeeded(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoNavigateToDetailPageIfNeeded(getIntent());
    }

    private void autoNavigateToDetailPageIfNeeded(Intent intent) {
        // TODO replace this stuff by a Intent + BackstackBuilder code when creating the notification.
        // If we were launched with OPEN_*, go to the requested page.
        // Use a delay before opening to make the sliding animation look better.
        final String channel = intent.getStringExtra("com.parse.Channel");
        if (PushPreferencesUpdater.CHANNEL_BULLETIN.equals(channel)) {
            messageHandler.sendMessageDelayed(
                    messageHandler.obtainMessage(MESSAGE_OPEN_PAGE, PAGE_BULLETIN, 0),
                    PAGE_OPEN_DELAY);
        } else if (PushPreferencesUpdater.CHANNEL_NEWSLETTER.equals(channel)) {
            messageHandler.sendMessageDelayed(
                    messageHandler.obtainMessage(MESSAGE_OPEN_PAGE, PAGE_NEWSLETTER, 0),
                    PAGE_OPEN_DELAY);
        }
    }

    @Override
    public void onItemSelected(int item) {
        switch (item) {
            case ITEM_AGENDA:
                DetailActivity.start(this, DetailActivity.MODE_AGENDA);
                break;
            case ITEM_BULLETIN:
                DetailActivity.start(this, DetailActivity.MODE_BULLETIN);
                break;
            case ITEM_CALL:
                callSebastiaan();
                break;
            case ITEM_HOME:
                GrabBag.openUri(this, getString(R.string.home_url));
                break;
            case ITEM_NEWSLETTER:
                DetailActivity.start(this, DetailActivity.MODE_NEWSLETTER);
                break;
            case ITEM_TEAM:
                DetailActivity.start(this, DetailActivity.MODE_TEAM);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_preferences) {
            DetailActivity.start(this, DetailActivity.MODE_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoading(Exception e) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MESSAGE_OPEN_PAGE) {
            if (msg.arg1 == PAGE_BULLETIN) {
                DetailActivity.start(this, DetailActivity.MODE_BULLETIN);
            } else {
                DetailActivity.start(this, DetailActivity.MODE_NEWSLETTER);
            }
            return true;
        }
        return false;
    }

    @Override
    public void downloadNewsletterFromUri(Uri uri) {
        if (newsletterDownloadHelper == null) {
            newsletterDownloadHelper = new NewsletterDownloadHelper(this);
        }
        newsletterDownloadHelper.downloadNewsletterFromUri(uri);
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
