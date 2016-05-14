package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;

/**
 * Wraps the {@link BackendApi#getTimeline()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class TimelineRVDataSource extends AbstractRVDataSource<TimelineItem> {

    private static TimelineRVDataSource instance;

    public static TimelineRVDataSource getInstance() {
        if (instance == null) {
            instance = new TimelineRVDataSource();
        }
        return instance;
    }

    private TimelineRVDataSource() {
        super();
    }

    @Override
    protected Observable<List<TimelineItem>> loadItems(BackendApi backend) {
        return backend.getTimeline();
    }
}
