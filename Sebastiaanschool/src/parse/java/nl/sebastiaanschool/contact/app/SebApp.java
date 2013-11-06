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
        ParseObject.registerSubclass(Newsletter.class);
        ParseObject.registerSubclass(TeamMember.class);
        Parse.setLogLevel(BuildConfig.DEBUG ? Parse.LOG_LEVEL_DEBUG : Parse.LOG_LEVEL_NONE);
        Parse.initialize(this, ParseConfig.APPLICATION_ID, ParseConfig.CLIENT_KEY);
        // TODO handle app crashes with analytics.
    }
}
