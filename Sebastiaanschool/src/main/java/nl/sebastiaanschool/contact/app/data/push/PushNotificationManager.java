package nl.sebastiaanschool.contact.app.data.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import nl.sebastiaanschool.contact.app.data.server.NotificationApi;

public class PushNotificationManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String PREF_PROMPT_SEEN = "push_prompt_seen";
    private static final String PREF_ENABLED = "push_enabled";
    private static final String PREF_USERNAME = "push_uuid0";
    private static final String PREF_PASSWORD = "push_uuid1";
    private static PushNotificationManager instance;
    private final NotificationApi backend;
    private final SharedPreferences pushPrefs;

    public static synchronized void init(Context context, NotificationApi bai) {
        if (instance == null) {
            instance = new PushNotificationManager(context.getApplicationContext(), bai);
        }
    }

    public static synchronized PushNotificationManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialised.");
        }
        return instance;
    }

    private PushNotificationManager(Context context, NotificationApi backend) {
        this.backend = backend;
        pushPrefs = context.getSharedPreferences("Sebastiaanschool_prefs", 0);
        pushPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    public void start() {

        // 0. Check if Google API client can run; main activity should prompt user to install/update if not

        // 1. Read user preferences, find first launch, push preference and username/password
        boolean userPrompted = pushPrefs.getBoolean(PREF_PROMPT_SEEN, true);

        // 2. If first launch: main activity should ask user if they want notifications and update PROMPT_SEEN

        // 4. If username and password known, retrieve push status to see if they work.
        String uuid0 = pushPrefs.getString(PREF_USERNAME, null);
        String uuid1 = pushPrefs.getString(PREF_PASSWORD, null);

        // 5. If username and password unknown, or failed, enroll

        // 6. Obtain firebase token.

        // 7. Submit firebase token and push-enabled setting.
        boolean wantsPush = pushPrefs.getBoolean(PREF_ENABLED, false);
        Log.i("SebApp", "Push: prompted=" + userPrompted + ", enabled=" + wantsPush + ", uuid1=" + uuid0 + ", uuid2=" + uuid1);
    }

    public boolean isPromptSeen() {
        return pushPrefs.getBoolean(PREF_ENABLED, false);
    }

    public void setPromptSeen() {
        pushPrefs.edit().putBoolean(PREF_PROMPT_SEEN, true).apply();
    }

    public boolean isUserInterested() {
        return pushPrefs.getBoolean(PREF_ENABLED, false);
    }

    private void setUserInterested(boolean interested) {
        pushPrefs.edit().putBoolean(PREF_ENABLED, interested).apply();
        // TODO trigger sync process at step 4.
    }

    void submitFirebaseInstanceId(@NonNull String firebaseIID) {
        // TODO trigger sync process at step 4.
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PREF_ENABLED.equals(key)) {
            boolean enabled = sharedPreferences.getBoolean(PREF_ENABLED, false);
            this.setUserInterested(enabled);
        }
    }
}
