package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.server.AgendaItem;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import rx.Observable;
import rx.functions.Func1;

/**
 * Wraps the {@link BackendApi#getAgenda()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class AgendaRVDataSource extends AbstractRVDataSource<AgendaItem> {

    private static AgendaRVDataSource instance;

    public static AgendaRVDataSource getInstance() {
        if (instance == null) {
            instance = new AgendaRVDataSource();
        }
        return instance;
    }

    private AgendaRVDataSource() {
        super();
    }

    @Override
    protected Observable<AgendaItem> loadItems(BackendApi backend) {
        return backend.getAgenda()
                .toObservable()
                .flatMap(new Func1<List<AgendaItem>, Observable<AgendaItem>>() {
            @Override
            public Observable<AgendaItem> call(List<AgendaItem> items) {
                return Observable.from(items);
            }
        });
    }
}
