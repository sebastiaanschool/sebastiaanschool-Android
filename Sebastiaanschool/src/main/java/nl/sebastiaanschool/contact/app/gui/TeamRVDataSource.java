package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Observable;

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
    protected Observable<List<TeamItem>> loadItems(BackendApi backend) {
        return backend.getTeam();
    }
}
