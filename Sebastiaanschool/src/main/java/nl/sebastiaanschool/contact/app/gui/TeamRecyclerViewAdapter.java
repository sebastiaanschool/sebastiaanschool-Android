package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * RecyclerView adapter for Agenda Items.
 */
class TeamRecyclerViewAdapter extends AbstractRVFragment.DestroyableRecyclerViewAdapter<TeamRecyclerViewAdapter.ViewHolder> {

    private final List<TeamItem> mValues;
    private final PublishSubject<TeamItem> itemsClicked = PublishSubject.create();
    private CompositeSubscription subscriptions = new CompositeSubscription();


    public TeamRecyclerViewAdapter(TeamDataSource agendaDataSource) {
        mValues = new ArrayList<>();
        subscriptions.add(agendaDataSource.getTeam()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TeamItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TimelineAdapter", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TimelineAdapter", "onError");
                    }

                    @Override
                    public void onNext(List<TeamItem> agendaItem) {
                        Log.d("TimelineAdapter", "onNext - " + agendaItem.size());
                        // TODO this is a bit blunt; I'm also unsure if the adapter should handle subscription.
                        mValues.clear();
                        mValues.addAll(agendaItem);
                        notifyDataSetChanged();
                    }
                }));
    }

    /**
     * A hot observable that emits items that have been tapped/clicked by the operator.
     * @return an observable.
     */
    public Observable<TeamItem> itemsClicked() {
        return itemsClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_team_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TeamItem item = mValues.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void onDestroy() {
        subscriptions.unsubscribe();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mName;
        public final TextView mDescription;
        public final TextView mEmail;
        public TeamItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setOnClickListener(this);
            mPicture = (ImageView) view.findViewById(R.id.item__picture);
            mPicture.setImageDrawable(GrabBag.loadVectorDrawable(mPicture.getContext(), R.drawable.ic_team_person_24dp));
            mName = (TextView) view.findViewById(R.id.item__name);
            mDescription = (TextView) view.findViewById(R.id.item__detail_text);
            mEmail = (TextView) view.findViewById(R.id.item__email);
        }

        public void setItem(TeamItem item) {
            this.mItem = item;
            this.mName.setText(item.displayName);
            this.mDescription.setText(item.detailText);
            this.mEmail.setText(item.email);
        }

        @Override
        public void onClick(View v) {
            itemsClicked.onNext(mItem);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
