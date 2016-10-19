package nl.sebastiaanschool.contact.app.data.server;

import com.squareup.moshi.Json;

public class PostPushSettingsRequest {
    public final String provider = "gcm";
    public boolean active;
    @Json(name = "registration_id")
    public String registrationId;
}
