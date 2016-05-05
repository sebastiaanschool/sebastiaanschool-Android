package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;

/**
 * Wraps the {@link BackendApi#getTimeline()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class TimelineDataSource {

    private static TimelineDataSource instance;

    public static TimelineDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new TimelineDataSource(context);
        }
        return instance;
    }

    private TimelineDataSource(Context context) {
        this.backend = BackendInterface.getInstance(context).connector;
    }

    private BackendApi backend;
    private Observable<List<TimelineItem>> timeline;

    public Observable<List<TimelineItem>> getTimeline() {
        return getTimeline(false);
    }

    public Observable<List<TimelineItem>> getTimeline(boolean force) {
        if (timeline == null || force) {
            timeline = backend.getTimeline().retry(2).cache();
        }
        return timeline;
    }
}
