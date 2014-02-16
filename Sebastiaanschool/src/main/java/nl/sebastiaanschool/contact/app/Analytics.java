/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
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
