package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * RecyclerView adapter for Agenda Items.
 */
class TeamRecyclerViewAdapter extends AbstractRVFragment.DestroyableRecyclerViewAdapter<TeamRecyclerViewAdapter.ViewHolder> {

    private final List<TeamItem> mValues;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mDescription;
        public final TextView mEmail;
        public TeamItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
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
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
