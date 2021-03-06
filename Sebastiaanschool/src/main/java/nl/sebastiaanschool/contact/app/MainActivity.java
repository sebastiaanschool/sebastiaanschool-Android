/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.danlew.android.joda.JodaTimeAndroid;

import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;
import nl.sebastiaanschool.contact.app.data.analytics.FirebaseWrapper;
import nl.sebastiaanschool.contact.app.data.downloadmanager.DownloadManagerInterface;
import nl.sebastiaanschool.contact.app.data.push.PushNotificationManager;
import nl.sebastiaanschool.contact.app.data.server.BackendInterface;
import nl.sebastiaanschool.contact.app.gui.NavigationPagerAdapter;

/**
 * Main entry point of our app.
 * <p/>
 * <p>The launch intent is checked for a String extra named {@code com.parse.Channel}. If this is
 * extra is found to contain either of the {@code PushPreferencesUpdater.PUSH_CHANNEL_} constants'
 * values, then the activity automatically navigates to the corresponding screen.</p>
 */
public class MainActivity extends AppCompatActivity {

    private AnalyticsInterface analytics;
    private ViewPager viewPager;
    private NavigationPagerAdapter viewPagerAdapter;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeStrictMode();
        initializeApplicationServices();
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));

        viewPager = (ViewPager) findViewById(R.id.main__content_container);
        viewPagerAdapter = new NavigationPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(viewPagerAdapter.enableAnalytics(analytics));

        TabLayout tl = (TabLayout) findViewById(R.id.detail_tabs);
        tl.setupWithViewPager(viewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (PushNotificationManager.isTimelineUpdateNotification(intent)) {
            // To test: adb shell am start -e from '/topics/sebastiaanschool.app.timeline' \
            //             -a ACTION_MAIN --activity-no-user-action \
            //             nl.sebastiaanschool.contact.app/.MainActivity
            if (viewPager != null) {
                viewPager.setCurrentItem(0, true);
                viewPagerAdapter.onTimelineUpdateNotificationTapped();
            }
        }
    }

    private void onTimelineUpdateBroadcastReceived() {
        if (viewPagerAdapter.isTimelineCurrentItem(viewPager)) {
            viewPagerAdapter.onTimelineUpdateBroadcastReceived();
        } else {
            final Snackbar sb = Snackbar.make(findViewById(android.R.id.content),
                    R.string.toast__new_timeline_items_timeline_not_visible, Snackbar.LENGTH_LONG);
            sb.setAction(R.string.toast__view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(0, true);
                    viewPagerAdapter.onTimelineUpdateNotificationTapped();
                    sb.dismiss();
                }
            });
            sb.show();
        }
    }

    private void initializeApplicationServices() {
        TraceCompat.beginSection("Initialize services");
        try {
            JodaTimeAndroid.init(this);
            BackendInterface.init(this);
            analytics = FirebaseWrapper.init(this);
            DownloadManagerInterface.init(this);
            PushNotificationManager.init(this, BackendInterface.getInstance().getNotificationApi());
            IntentFilter filter = PushNotificationManager.createTimelineUpdateBroadcastFilter();
            LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, filter);
        } finally {
            TraceCompat.endSection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManagerInterface.getInstance().destroy(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
    }

    private void initializeStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .penaltyDeathOnNetwork()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .setClassInstanceLimit(BackendInterface.class, 1)
                    .setClassInstanceLimit(DownloadManagerInterface.class, 1)
                    .build());
        }
    }

    private final BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PushNotificationManager.isTimelineUpdateBroadcast(intent)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTimelineUpdateBroadcastReceived();
                    }
                });
            }
        }
    };
}
