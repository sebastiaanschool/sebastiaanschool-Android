package nl.sebastiaanschool.contact.app.data.server;

import java.util.List;

import retrofit2.http.GET;
import rx.Single;

/**
 * Retrofit binding interface for getting our data.
 */
public interface BackendApi {
    @GET("/api/timeline/")
    Single<List<TimelineItem>> getTimeline();

    @GET("/api/agendaItems/")
    Single<List<AgendaItem>> getAgenda();

    /**
     * Here's a mismatch between the API URL and the Java class names. This is unfortunate, but I
     * don't know what else to call the contact tab, therefore the tab showing this stuff will be
     * "team".
     */
    @GET("/api/contactItems/")
    Single<List<TeamItem>> getTeam();
}
