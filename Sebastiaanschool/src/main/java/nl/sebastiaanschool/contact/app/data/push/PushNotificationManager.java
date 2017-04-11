package nl.sebastiaanschool.contact.app.data.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import nl.sebastiaanschool.contact.app.data.server.EnrollmentRequest;
import nl.sebastiaanschool.contact.app.data.server.GetPushSettingsResponse;
import nl.sebastiaanschool.contact.app.data.server.NotificationApi;
import nl.sebastiaanschool.contact.app.data.server.PostPushSettingsRequest;
import nl.sebastiaanschool.contact.app.data.server.PostPushSettingsResponse;
import okhttp3.Credentials;
import retrofit2.adapter.rxjava.HttpException;
import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PushNotificationManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TIMELINE_UPDATED = "sebastiaanschool.Broadcast.TimelineUpdated";
    private static final String FIREBASE_TOPIC = "sebastiaanschool.app.timeline";
    private static final String FIREBASE_TOPIC_REF = "/topics/sebastiaanschool.app.timeline";
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
            instance.sync();
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

    /**
     * Returns whether the given remote message is a timeline update broadcast.
     */
    public static boolean isTimelineUpdateBroadcast(Intent intent) {
        return intent != null
                && TIMELINE_UPDATED.equals(intent.getAction());
    }

    /**
     * Returns whether the given remote message is a timeline update notification.
     */
    public static boolean isTimelineUpdateMessage(RemoteMessage message) {
        return message != null
                && FIREBASE_TOPIC_REF.equals(message.getFrom());
    }

    /**
     * Returns whether the given intent is a timeline update notification tapped.
     */
    public static boolean isTimelineUpdateNotification(Intent intent) {
        return intent != null
                && intent.hasExtra("from")
                && FIREBASE_TOPIC_REF.equals(intent.getStringExtra("from"));
    }

    /**
     * Returns an intent filter that matches timeline update broadcasts.
     */
    public static IntentFilter createTimelineUpdateBroadcastFilter() {
        return new IntentFilter(TIMELINE_UPDATED);
    }

    /**
     * Returns a broadcast intent that indicates that new timeline data is available.
     */
    public static Intent createTimelineUpdateBroadcast(Context context) {
        return new Intent(TIMELINE_UPDATED);
    }

    // https://developers.google.com/android/guides/api-client#ignoring_api_connection_failures

    // TODO handle the Play Services Unavailable scenario (GoogleApiAvailability; getErrorDialog)
    // TODO on first launch: request user permission to enable push messaging

    public boolean isPromptSeen() {
        return pushPrefs.getBoolean(PREF_ENABLED, false);
    }

    public void setPromptSeen() {
        pushPrefs.edit().putBoolean(PREF_PROMPT_SEEN, true).apply();
    }

    void onFirebaseInstanceIdChanged() {
        sync();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PREF_ENABLED.equals(key)) {
            sync();
        }
    }

    private void sync() {
        final String username = pushPrefs.getString(PREF_USERNAME, null);
        final String password = pushPrefs.getString(PREF_PASSWORD, null);
        authorize(username, password)
            .onErrorResumeNext(enrollIfNeeded)
            .flatMap(submitPushPreference)
            .subscribeOn(Schedulers.io())
            .subscribe(syncSubscriber);
    }

    /**
     * Check credentials, return authorization.
     * @param username a username.
     * @param password a password.
     * @return an HTTP Authorization header value.
     */
    private Single<String> authorize(String username, String password) {
        if (username == null || password == null) {
            return Single.error(new NoStoredCredentialsException());
        }
        final String credential = Credentials.basic(username, password);
        return backend.getPushSettings(credential)
                .map(new Func1<GetPushSettingsResponse, String>() {
                    @Override
                    public String call(GetPushSettingsResponse getPushSettingsResponse) {
                        return credential;
                    }
                });
    }

    /**
     * If the given throwable is one that justifies (re-)enrollment, enroll.
     */
    private Func1<Throwable, Single<String>> enrollIfNeeded = new Func1<Throwable, Single<String>>() {
        @Override
        public Single<String> call(Throwable t) {
            if (t instanceof NoStoredCredentialsException) {
                return enroll();
            }
            if ((t instanceof HttpException) && ((HttpException) t).code() == 403) {
                FirebaseCrash.log("Auth failure of stored credentials");
                return enroll();
            }
            return Single.error(t);
        }
    };

    /**
     * Generate username and password, enroll with backend, store username and password in prefs.
     * @return an HTTP Authorization header value.
     */
    private Single<String> enroll() {
        final String username = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();
        return backend.enroll(new EnrollmentRequest(username, password))
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String dummy) {
                        pushPrefs.edit()
                                .putString(PREF_USERNAME, username)
                                .putString(PREF_PASSWORD, password)
                                .apply();
                        return Credentials.basic(username, password);
                    }
                });
    }

    /**
     * Submit the current push notification preference to Firebase and to our backend.
     */
    private Func1<String, Single<PostPushSettingsResponse>> submitPushPreference =
            new Func1<String, Single<PostPushSettingsResponse>>() {
        @Override
        public Single<PostPushSettingsResponse> call(String authorization) {
            final boolean enable = pushPrefs.getBoolean(PREF_ENABLED, false);
            final String token = FirebaseInstanceId.getInstance().getToken();
            if (enable) {
                FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_TOPIC);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(FIREBASE_TOPIC);
            }
            PostPushSettingsRequest request = new PostPushSettingsRequest(enable, token);
            return backend.postPushSettings(authorization, request);
        }
    };

    private SingleSubscriber<PostPushSettingsResponse> syncSubscriber = new SingleSubscriber<PostPushSettingsResponse>() {
        @Override
        public void onSuccess(PostPushSettingsResponse response) {
            Log.d("SebApp", "Push sync: Successful (active=" + response.active + ")");
        }

        @Override
        public void onError(Throwable error) {
            Log.w("SebApp", "Push sync: Failed", error);
            FirebaseCrash.log("Failed to sync push token: " + error);
        }
    };

    private static class NoStoredCredentialsException extends RuntimeException {}
}
