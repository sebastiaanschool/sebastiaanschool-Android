/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.widget.ListAdapter;

/**
 * Created by barend on 6-11-13.
 */
public interface SebListAdapter extends ListAdapter {
    void setDataLoadingCallback(DataLoadingCallback dlc);
    void loadData();

}
