package nl.sebastiaanschool.contact.app;

import android.content.Intent;

import com.parse.ParseAnalytics;

/**
 * Wraps the {@code ParseAnalytics} class so that it can be kept out of the "stubbed" flavor.
 * Doesn't make any attempt at hiding the underlying implementation.
 * Created by barend on 4-11-13.
 */
public class Analytics {

    public static void trackAppOpened(Intent intent) {
        ParseAnalytics.trackAppOpened(intent);
    }

    public static void trackEvent(String name) {
        ParseAnalytics.trackEvent(name);
    }
}
