package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Observable;
import rx.functions.Func1;

/**
 * Wraps the {@link BackendApi#getTeam()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class TeamRVDataSource extends AbstractRVDataSource<TeamItem> {

    private static TeamRVDataSource instance;

    public static TeamRVDataSource getInstance() {
        if (instance == null) {
            instance = new TeamRVDataSource();
        }
        return instance;
    }

    private TeamRVDataSource() {
        super();
    }

    @Override
    protected Observable<TeamItem> loadItems(BackendApi backend) {
        return backend.getTeam()
                .toObservable()
                .flatMap(new Func1<List<TeamItem>, Observable<TeamItem>>() {
            @Override
            public Observable<TeamItem> call(List<TeamItem> items) {
                return Observable.from(items);
            }
        });
    }
}
