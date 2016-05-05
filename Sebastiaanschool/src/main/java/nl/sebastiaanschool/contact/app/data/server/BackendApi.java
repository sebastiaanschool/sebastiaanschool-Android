package nl.sebastiaanschool.contact.app.data.server;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Retrofit binding interface for our backend service.
 */
public interface BackendApi {

    @GET("/api/timeline")
    Observable<List<TimelineItem>> getTimeline();
}
