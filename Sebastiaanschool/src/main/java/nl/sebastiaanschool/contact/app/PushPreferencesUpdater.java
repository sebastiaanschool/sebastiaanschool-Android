package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.parse.PushService;

import java.util.Set;

/**
 * Reads the user's push preferences from SharedPrefs and updates the Parse subscriptions to match.
 */
public class PushPreferencesUpdater {

    public static final String CHANNEL_BULLETIN = "bulletin-android";
    public static final String CHANNEL_NEWSLETTER = "newsletter-android";
    public static final String PREF_NEWSLETTERS = "pref_push_newsletters";
    public static final String PREF_BULLETINS = "pref_push_bulletins";

    private Context context;

    public PushPreferencesUpdater(Context context) {
        this.context = context.getApplicationContext();
    }

    public void updatePushPreferences() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean subscribeNewsletter = prefs.getBoolean(PREF_NEWSLETTERS, true);
        final boolean subscribeBulletin = prefs.getBoolean(PREF_BULLETINS, true);
        final Set<String> subscriptions = PushService.getSubscriptions(context);
        if (BuildConfig.DEBUG) {
            android.util.Log.d("PushPrefUpdater", "Current subscriptions: " + subscriptions);
        }
        updatePushes(subscriptions, CHANNEL_BULLETIN, subscribeBulletin);
        updatePushes(subscriptions, CHANNEL_NEWSLETTER, subscribeNewsletter);
    }

    private void updatePushes(Set<String> subscriptions, String channel, boolean shouldBeSubscribed) {
        final boolean isSubscribed = subscriptions.contains(channel);
        if (BuildConfig.DEBUG) {
            android.util.Log.d("PushPrefUpdater", String.format("Channel=%s, isSubscribed=%s, shouldSubscribe=%s", channel, isSubscribed, shouldBeSubscribed));
        }
        if (isSubscribed && !shouldBeSubscribed) {
            PushService.unsubscribe(context, channel);
        } else if (shouldBeSubscribed && !isSubscribed) {
            PushService.subscribe(context, channel, MainActivity.class, R.drawable.ic_push_ntf);
        }
    }
}
