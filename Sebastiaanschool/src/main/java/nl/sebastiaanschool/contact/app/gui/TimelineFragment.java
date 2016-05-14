package nl.sebastiaanschool.contact.app.gui;

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends AbstractRVFragment<TimelineRecyclerViewAdapter> {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    @SuppressWarnings("unused")
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    @Override
    protected TimelineRecyclerViewAdapter createAdapter() {
        return new TimelineRecyclerViewAdapter(TimelineRVDataSource.getInstance());
    }
}
