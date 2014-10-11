/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Application;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Initializes the Parse SDK client.
 */
public class SebApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(AgendaItem.class);
        ParseObject.registerSubclass(Bulletin.class);
        ParseObject.registerSubclass(Newsletter.class);
        ParseObject.registerSubclass(TeamMember.class);
        Parse.setLogLevel(BuildConfig.DEBUG ? Parse.LOG_LEVEL_DEBUG : Parse.LOG_LEVEL_NONE);
        final String applicationId = BuildConfig.PARSE_APPLICATION_ID;
        final String clientKey = BuildConfig.PARSE_CLIENT_KEY;
        Parse.initialize(this, applicationId, clientKey);
        if (applicationId == null || clientKey == null) {
            final Toast toast = Toast.makeText(this, "NO PARSE API KEY DEFINED!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
            currentInstallation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        new PushPreferencesUpdater(SebApp.this).updatePushPreferences();
                    }
                }
            });
        }
    }
}
