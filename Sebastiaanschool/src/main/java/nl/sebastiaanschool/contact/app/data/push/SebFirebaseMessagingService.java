package nl.sebastiaanschool.contact.app.data.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class SebFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("SebApp", "onMessageReceived: " + remoteMessage.getMessageId());
        // TODO handle notification
    }
}
