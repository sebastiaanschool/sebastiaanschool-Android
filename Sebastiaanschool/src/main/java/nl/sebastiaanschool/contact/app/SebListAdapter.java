package nl.sebastiaanschool.contact.app;

import android.widget.ListAdapter;

/**
 * Created by barend on 6-11-13.
 */
public interface SebListAdapter extends ListAdapter {
    void setDataLoadingCallback(DataLoadingCallback dlc);
    void loadData();

}
