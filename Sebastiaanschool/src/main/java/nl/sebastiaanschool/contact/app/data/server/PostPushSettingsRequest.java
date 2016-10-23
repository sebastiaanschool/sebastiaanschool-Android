package nl.sebastiaanschool.contact.app.data.server;

import android.os.Build;

import com.squareup.moshi.Json;

public class PostPushSettingsRequest {
    public final String service = "gcm";
    public final String name;
    public final boolean active;
    @Json(name = "registration_id")
    public final String registrationId;

    public PostPushSettingsRequest(boolean active, String registrationId) {
        this.active = active;
        this.registrationId = registrationId;
        String rawName = Build.MANUFACTURER + " " + Build.MODEL;
        this.name = rawName.length() < 256 ? rawName : rawName.substring(0, 255);
    }

}
