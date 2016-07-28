package nl.sebastiaanschool.contact.app.gui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.server.TeamItem;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * RecyclerView adapter for Agenda Items.
 */
class TeamRVAdapter extends AbstractRVAdapter<TeamItem, TeamRVAdapter.ViewHolder> {

    private final PublishSubject<TeamItem> itemsClicked = PublishSubject.create();


    public TeamRVAdapter(TeamRVDataSource teamDataSource, Listener listener) {
        super(teamDataSource, listener);
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
        final TeamItem item = itemsShowing.get(position);
        holder.setItem(item);
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
    }
}
