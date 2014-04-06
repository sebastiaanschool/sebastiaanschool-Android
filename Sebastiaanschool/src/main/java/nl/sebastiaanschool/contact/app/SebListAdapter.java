/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.widget.ListAdapter;

/**
 * Created by barend on 6-11-13.
 */
public interface SebListAdapter extends ListAdapter {
    void setDataLoadingCallback(DataLoadingCallback dlc);
}
