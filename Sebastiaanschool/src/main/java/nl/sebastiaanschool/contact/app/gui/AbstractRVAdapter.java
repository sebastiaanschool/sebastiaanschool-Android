package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class for binding data to {@link AbstractRVFragment}s in the GUI.
 * @param <I> the data type shown.
 */
public abstract class AbstractRVAdapter<I, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final CompositeSubscription subscriptions = new CompositeSubscription();
    protected final List<I> items = new ArrayList<>(32);
    protected final AbstractRVDataSource<I> dataSource;

    protected AbstractRVAdapter(AbstractRVDataSource<I> dataSource) {
        this.dataSource = dataSource;
        subscriptions.add(dataSource.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<I>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TimelineAdapter", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TimelineAdapter", "onError");
                    }

                    @Override
                    public void onNext(List<I> agendaItem) {
                        items.clear();
                        items.addAll(agendaItem);
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
}
