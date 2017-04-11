package nl.sebastiaanschool.contact.app.data.server;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Completable;
import rx.Single;

/**
 * Retrofit binding interface for the push messaging registration.
 */
public interface NotificationApi {

    // Note: this returns an rx.Single<String> to avoid a Retrofit bug.
    // https://github.com/square/retrofit/issues/1864
    // A more appropriate return type would be rx.Completable.
    @POST("/api/enrollment")
    Single<String> enroll(@Body EnrollmentRequest request);

    @DELETE("/api/enrollment")
    Completable disenroll(@Header("Authorization") String authorization);

    @GET("/api/push-settings")
    Single<GetPushSettingsResponse> getPushSettings(@Header("Authorization") String authorization);

    @POST("/api/push-settings")
    Single<PostPushSettingsResponse> postPushSettings(@Header("Authorization") String authorization,
                                 @Body PostPushSettingsRequest request);
}
