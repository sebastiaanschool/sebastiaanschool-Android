package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class for binding data to {@link AbstractRVFragment}s in the GUI.
 * @param <I> the data type shown.
 */
abstract class AbstractRVAdapter<I, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final CompositeSubscription subscriptions = new CompositeSubscription();
    protected final AbstractRVDataSource<I> dataSource;
    protected final List<I> itemsShowing = new ArrayList<>(32);
    private final List<I> itemsLoading = new ArrayList<>(32);
    private Listener listener;

    protected AbstractRVAdapter(AbstractRVDataSource<I> dataSource, Listener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
        subscribe(dataSource.getItems());
    }

    void refresh() {
        subscribe(dataSource.getItems(true));
    }

    private void subscribe(Observable<I> itemListObservable) {
        listener.startedLoadingData();
        itemsLoading.clear();
        subscriptions.add(itemListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        listener.finishedLoadingData(true);
                    }
                })
                .subscribe(new Subscriber<I>() {
                    @Override
                    public void onCompleted() {
                        AbstractRVAdapter.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(I item) {
                        AbstractRVAdapter.this.onNext(item);
                    }
                }));
    }

    protected void onNext(I item) {
        AbstractRVAdapter.this.itemsLoading.add(item);
    }

    protected void onCompleted() {
        itemsShowing.clear();
        itemsShowing.addAll(itemsLoading);
        itemsLoading.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemsShowing.size();
    }

    protected void onDestroy() {
        subscriptions.unsubscribe();
        itemsShowing.clear();
        itemsLoading.clear();
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
