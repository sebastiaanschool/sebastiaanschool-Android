package nl.sebastiaanschool.contact.app.data.server;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Retrofit binding interface for our backend service.
 */
public interface BackendApi {

    @GET("/api/timeline/")
    Observable<List<TimelineItem>> getTimeline();

    @GET("/api/agendaItems/")
    Observable<List<AgendaItem>> getAgenda();

    /**
     * Here's a mismatch between the API URL and the Java class names. This is unfortunate, but I
     * don't know what else to call the contact tab, therefore the tab showing this stuff will be
     * "team".
     */
    @GET("/api/contactItems/")
    Observable<List<TeamItem>> getTeam();
}
