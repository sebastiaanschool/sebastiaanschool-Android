/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeStrictMode();
        initializeApplicationServices();
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));

        final ViewPager vp = (ViewPager) findViewById(R.id.main__content_container);
        final NavigationPagerAdapter pagerAdapter =
                new NavigationPagerAdapter(this, getSupportFragmentManager(), analytics);
        vp.setAdapter(pagerAdapter);
        vp.setOffscreenPageLimit(5);
        vp.addOnPageChangeListener(pagerAdapter.analyticsListener());

        TabLayout tl = (TabLayout) findViewById(R.id.detail_tabs);
        tl.setupWithViewPager(vp);
    }

    private void initializeApplicationServices() {
        TraceCompat.beginSection("Initialize services");
        try {
            JodaTimeAndroid.init(this);
            BackendInterface.init(this);
            analytics = FirebaseWrapper.init(this);
            DownloadManagerInterface.init(this);
            PushNotificationManager.init(this, BackendInterface.getInstance().getNotificationApi());
        } finally {
            TraceCompat.endSection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManagerInterface.getInstance().destroy(this);
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
}
