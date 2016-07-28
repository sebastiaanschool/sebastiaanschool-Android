package nl.sebastiaanschool.contact.app.gui;

import java.util.List;

import nl.sebastiaanschool.contact.app.data.BackendInterface;
import nl.sebastiaanschool.contact.app.data.server.BackendApi;
import rx.Observable;

/**
 * Base class for bringing data to {@link AbstractRVFragment}s in the GUI. Instances of this class
 * live should be created as singletons so they can be the bit stays in place when the GUI is
 * rebuilt to respond to a configuration change.
 *
 * @param <I> the data type returned by this data source.
 */
abstract class AbstractRVDataSource<I> {

    protected AbstractRVDataSource() {
        backend = BackendInterface.getInstance().connector;
    }

    private BackendApi backend;
    private Observable<I> items;

    public Observable<I> getItems() {
        return getItems(false);
    }

    public Observable<I> getItems(boolean force) {
        if (items == null || force) {
            items = loadItems(backend).retry(2).cache();
        }
        return items;
    }

    protected abstract Observable<I> loadItems(BackendApi backend);
}
