package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TimelineItem;
import rx.Observable;
import rx.functions.Func1;

/**
 * Wraps the {@link BackendApi#getTimeline()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class TimelineRVDataSource extends AbstractRVDataSource<TimelineItemViewModel> {

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
    protected Observable<TimelineItemViewModel> loadItems(BackendApi backend) {
        return backend.getTimeline()
                .toObservable()
                .flatMap(new Func1<List<TimelineItem>, Observable<TimelineItem>>() {
                    @Override
                    public Observable<TimelineItem> call(List<TimelineItem> items) {
                        return Observable.from(items);
                    }
                })
                .map(new Func1<TimelineItem, TimelineItemViewModel>() {
                    @Override
                    public TimelineItemViewModel call(TimelineItem item) {
                        return new TimelineItemViewModel(item);
                    }
                });
    }
}
