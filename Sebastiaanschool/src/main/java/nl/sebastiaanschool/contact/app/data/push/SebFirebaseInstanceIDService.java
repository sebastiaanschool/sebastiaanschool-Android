package nl.sebastiaanschool.contact.app.data.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class SebFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            try {
                PushNotificationManager.getInstance().onFirebaseInstanceIdChanged();
            } catch (IllegalStateException e) {
                // Race condition: we beat PushNotificationManager in initialization order.
                // This is harmless; PNM will obtain the refreshedToken in its own time.
            }
        }
    }
}
