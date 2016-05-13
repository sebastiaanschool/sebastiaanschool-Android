package nl.sebastiaanschool.contact.app.gui;


public class TeamFragment extends AbstractRVFragment<TeamRecyclerViewAdapter> {

    public TeamFragment() {
        // Required empty public constructor
    }

    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    @Override
    protected TeamRecyclerViewAdapter createAdapter() {
        return new TeamRecyclerViewAdapter(TeamDataSource.getInstance());
    }
}
