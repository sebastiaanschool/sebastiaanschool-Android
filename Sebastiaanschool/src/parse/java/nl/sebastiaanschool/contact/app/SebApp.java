package nl.sebastiaanschool.contact.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by barend on 3-11-13.
 */
public class SebApp extends Application {

    private static final String APPLICATION_ID = "your app ID could be in here!";
    private static final String CLIENT_KEY = "just enter it and you're set!";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(AgendaItem.class);
        Parse.setLogLevel(BuildConfig.DEBUG ? Parse.LOG_LEVEL_DEBUG : Parse.LOG_LEVEL_NONE);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }
}
