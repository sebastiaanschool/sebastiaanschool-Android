package nl.sebastiaanschool.contact.app.gui;


public class AgendaFragment extends AbstractRVFragment<AgendaRecyclerViewAdapter> {

    public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance() {
        return new AgendaFragment();
    }

    @Override
    protected AgendaRecyclerViewAdapter createAdapter() {
        return new AgendaRecyclerViewAdapter(AgendaDataSource.getInstance());
    }
}
