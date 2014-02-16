/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.Set;

/**
 * Created by barend on 3-11-13.
 */
public class SebApp extends Application {

    public static final String PUSH_CHANNEL_BULLETIN = "bulletin-android";
    public static final String PUSH_CHANNEL_NEWSLETTER = "newsletter-android";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(AgendaItem.class);
        ParseObject.registerSubclass(Bulletin.class);
        ParseObject.registerSubclass(Newsletter.class);
        ParseObject.registerSubclass(TeamMember.class);
        Parse.setLogLevel(BuildConfig.DEBUG ? Parse.LOG_LEVEL_DEBUG : Parse.LOG_LEVEL_NONE);
        // The .toString() calls force an app crash at startup if the required environment variables were
        // not set during build. This is the closest I could get to Fail Fast Behaviour without rendering
        // Android Studio unusable.
        Parse.initialize(this, BuildConfig.APPLICATION_ID.toString(), BuildConfig.CLIENT_KEY.toString());
        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        currentInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    PushService.setDefaultPushCallback(getApplicationContext(), MainActivity.class, R.drawable.ic_push_ntf);
                    final Set<String> subscriptions = PushService.getSubscriptions(SebApp.this);
                    if (!subscriptions.contains(PUSH_CHANNEL_BULLETIN)) {
                        PushService.subscribe(getApplicationContext(), PUSH_CHANNEL_BULLETIN, MainActivity.class, R.drawable.ic_push_ntf);
                    }
                    if (!subscriptions.contains(PUSH_CHANNEL_NEWSLETTER)) {
                        PushService.subscribe(getApplicationContext(), PUSH_CHANNEL_NEWSLETTER, MainActivity.class, R.drawable.ic_push_ntf);
                    }
                }
            }
        });
    }
}
