package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;

/**
 * Wraps the {@link BackendApi#getAgenda()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class AgendaDataSource {

    private static AgendaDataSource instance;

    public static AgendaDataSource getInstance() {
        if (instance == null) {
            instance = new AgendaDataSource();
        }
        return instance;
    }

    private AgendaDataSource() {
        this.backend = BackendInterface.getInstance().connector;
    }

    private BackendApi backend;
    private Observable<List<AgendaItem>> timeline;

    public Observable<List<AgendaItem>> getAgenda() {
        return getAgenda(false);
    }

    public Observable<List<AgendaItem>> getAgenda(boolean force) {
        if (timeline == null || force) {
            timeline = backend.getAgenda().retry(2).cache();
        }
        return timeline;
    }
}
