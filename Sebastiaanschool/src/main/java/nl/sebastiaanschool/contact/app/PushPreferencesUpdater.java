package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.Collection;

/**
 * Reads the user's push preferences from SharedPrefs and updates the Parse subscriptions to match.
 */
public class PushPreferencesUpdater {

    public static final String CHANNEL_BULLETIN = "bulletin-android";
    public static final String CHANNEL_NEWSLETTER = "newsletter-android";
    private static final String PREF_NEWSLETTERS = "pref_push_newsletters";
    private static final String PREF_BULLETINS = "pref_push_bulletins";

    private final Context context;

    public PushPreferencesUpdater(Context context) {
        this.context = context.getApplicationContext();
    }

    public void updatePushPreferences() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean subscribeNewsletter = prefs.getBoolean(PREF_NEWSLETTERS, true);
        final boolean subscribeBulletin = prefs.getBoolean(PREF_BULLETINS, true);
        final Collection<String> subscriptions = ParseInstallation.getCurrentInstallation().getList("channels");
        if (BuildConfig.DEBUG) {
            android.util.Log.d("PushPrefUpdater", "Current subscriptions: " + subscriptions);
        }
        updatePushes(subscriptions, CHANNEL_BULLETIN, subscribeBulletin);
        updatePushes(subscriptions, CHANNEL_NEWSLETTER, subscribeNewsletter);
    }

    private void updatePushes(Collection<String> subscriptions, String channel, boolean shouldBeSubscribed) {
        final boolean isSubscribed = subscriptions != null && subscriptions.contains(channel);
        if (BuildConfig.DEBUG) {
            android.util.Log.d("PushPrefUpdater", String.format("Channel=%s, isSubscribed=%s, shouldSubscribe=%s", channel, isSubscribed, shouldBeSubscribed));
        }
        if (isSubscribed && !shouldBeSubscribed) {
            ParsePush.unsubscribeInBackground(channel);
        } else if (shouldBeSubscribed && !isSubscribed) {
            ParsePush.subscribeInBackground(channel);
        }
    }
}
