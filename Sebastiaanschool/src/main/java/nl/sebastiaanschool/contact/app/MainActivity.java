/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import nl.sebastiaanschool.contact.app.gui.NavigationPagerAdapter;

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

        getApplicationContext().registerReceiver(downloadCompletionReceiver, DOWNLOAD_COMPLETED_BROADCASTS);
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().unregisterReceiver(downloadCompletionReceiver);
        super.onDestroy();
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
