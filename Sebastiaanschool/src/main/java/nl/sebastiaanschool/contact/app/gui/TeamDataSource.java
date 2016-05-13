package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Observable;

/**
 * Wraps the {@link BackendApi#getTeam()} in a caching observable so that we can disconnect and
 * reconnect during lifecycle changes.
 */
class TeamDataSource {

    private static TeamDataSource instance;

    public static TeamDataSource getInstance() {
        if (instance == null) {
            instance = new TeamDataSource();
        }
        return instance;
    }

    private TeamDataSource() {
        this.backend = BackendInterface.getInstance().connector;
    }

    private BackendApi backend;
    private Observable<List<TeamItem>> team;

    public Observable<List<TeamItem>> getTeam() {
        return getTeam(false);
    }

    public Observable<List<TeamItem>> getTeam(boolean force) {
        if (team == null || force) {
            team = backend.getTeam().retry(2).cache();
        }
        return team;
    }
}
