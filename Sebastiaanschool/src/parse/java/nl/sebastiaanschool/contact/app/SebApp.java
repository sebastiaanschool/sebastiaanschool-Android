/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by barend on 3-11-13.
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
        // The .toString() calls force an app crash at startup if the required environment variables were
        // not set during build. This is the closest I could get to Fail Fast Behaviour without rendering
        // Android Studio unusable.
        Parse.initialize(this, ParseConfig.APPLICATION_ID.toString(), ParseConfig.CLIENT_KEY.toString());
    }
}
