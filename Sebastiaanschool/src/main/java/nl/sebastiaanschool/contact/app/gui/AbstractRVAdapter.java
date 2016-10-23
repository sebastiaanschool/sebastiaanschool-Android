package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static nl.sebastiaanschool.contact.app.gui.GrabBag.assertOnMainThread;

/**
 * Base class for binding data to {@link AbstractRVFragment}s in the GUI.
 * @param <I> the data type shown.
 */
abstract class AbstractRVAdapter<I, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final CompositeSubscription subscriptions = new CompositeSubscription();
    protected final AbstractRVDataSource<I> dataSource;
    /** Access on main thread only */
    protected final List<I> itemsShowing = new ArrayList<>(32);
    /** Access on main thread only */
    private final List<I> itemsLoading = new ArrayList<>(32);
    private final Listener listener;
    /** Access on main thread only */
    private boolean refreshing;

    protected AbstractRVAdapter(AbstractRVDataSource<I> dataSource, Listener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
        subscribe(dataSource.getItems());
    }

    void refresh() {
        assertOnMainThread();
        if (refreshing) {
            throw new IllegalStateException("refresh in progress");
        }
        subscribe(dataSource.getItems(true));
    }

    boolean isRefreshing() {
        assertOnMainThread();
        return refreshing;
    }

    private void subscribe(Observable<I> itemListObservable) {
        assertOnMainThread();
        refreshing = true;
        listener.startedLoadingData();
        itemsLoading.clear();
        subscriptions.add(itemListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        listener.finishedLoadingData();
                    }
                })
                .subscribe(new Subscriber<I>() {
                    @Override
                    public void onCompleted() {
                        AbstractRVAdapter.this.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AbstractRVAdapter.this.onError(e);
                    }

                    @Override
                    public void onNext(I item) {
                        AbstractRVAdapter.this.onNext(item);
                    }
                }));
    }

    protected void onNext(I item) {
        assertOnMainThread();
        AbstractRVAdapter.this.itemsLoading.add(item);
    }

    protected void onCompleted() {
        assertOnMainThread();
        itemsShowing.clear();
        itemsShowing.addAll(itemsLoading);
        itemsLoading.clear();
        notifyDataSetChanged();
        refreshing = false;
    }

    protected void onError(Throwable e) {
        FirebaseCrash.logcat(Log.DEBUG, "ARVA", "ARVA Last-Resort: " + e.toString());
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

        void finishedLoadingData();
    }
}
