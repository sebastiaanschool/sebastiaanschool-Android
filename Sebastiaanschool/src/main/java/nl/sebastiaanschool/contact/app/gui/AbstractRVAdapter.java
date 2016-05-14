package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class for binding data to {@link AbstractRVFragment}s in the GUI.
 * @param <I> the data type shown.
 */
abstract class AbstractRVAdapter<I, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final CompositeSubscription subscriptions = new CompositeSubscription();
    protected final List<I> items = new ArrayList<>(32);
    protected final AbstractRVDataSource<I> dataSource;
    private Listener listener;

    protected AbstractRVAdapter(AbstractRVDataSource<I> dataSource, Listener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
        subscribe(dataSource.getItems());
    }

    void refresh() {
        subscribe(dataSource.getItems(true));
    }

    private void subscribe(Observable<List<I>> itemListObservable) {
        listener.startedLoadingData();
        subscriptions.add(itemListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<I>>() {
                    @Override
                    public void onCompleted() {
                        listener.finishedLoadingData(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.finishedLoadingData(false);
                    }

                    @Override
                    public void onNext(List<I> agendaItem) {
                        AbstractRVAdapter.this.items.clear();
                        AbstractRVAdapter.this.items.addAll(agendaItem);
                        notifyDataSetChanged();
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void onDestroy() {
        subscriptions.unsubscribe();
    }

    /**
     * Notification interface for data loading events. I could have used rx, but the fragment,
     * RecyclerView and SwipeRefreshLayout are so tightly interwoven that I don't need the
     * flexibility. This saves me from having to define an event type.
     */
    interface Listener {
        void startedLoadingData();

        void finishedLoadingData(boolean successfully);
    }
}
