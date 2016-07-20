package nl.sebastiaanschool.contact.app.gui;

import java.util.ArrayList;
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
    protected Observable<List<TimelineItemViewModel>> loadItems(BackendApi backend) {
        return backend.getTimeline().map(WRAP_IN_VIEW_MODEL);
    }

    private static Func1<List<TimelineItem>, List<TimelineItemViewModel>> WRAP_IN_VIEW_MODEL = new Func1<List<TimelineItem>, List<TimelineItemViewModel>>() {
        @Override
        public List<TimelineItemViewModel> call(List<TimelineItem> timelineItems) {
            List<TimelineItemViewModel> output = new ArrayList<>(timelineItems.size());
            for (TimelineItem item: timelineItems) {
                output.add(new TimelineItemViewModel(item));
            }
            return output;
        }
    };
}
