package nl.sebastiaanschool.contact.app.data.push;

import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class SebFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (PushNotificationManager.isTimelineUpdateMessage(remoteMessage)) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                    PushNotificationManager.createTimelineUpdateBroadcast(this));
        }
        // We discard any other notifications.
    }
}
