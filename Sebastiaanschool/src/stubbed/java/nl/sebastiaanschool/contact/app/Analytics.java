/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Intent;

/**
 * No-op wrapper for the {@code com.parse.ParseAnalytics class}.
 * Created by barend on 4-11-13.
 */
public class Analytics {

    public static void trackAppOpened(Intent intent) {
        // No-op
    }

    public static void trackEvent(String name) {
        // No-op
    }
}
