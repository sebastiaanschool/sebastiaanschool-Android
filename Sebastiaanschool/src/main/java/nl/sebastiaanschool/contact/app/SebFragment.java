/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Base class for fragments in this app.
 * Created by Barend on 2-11-13.
 */
public abstract class SebFragment extends Fragment {

    /**
     * Configures a fragment transaction to make the sliding appearance work.
     *
     * @param tx             a fragment transaction (required).
     * @param containerResId the container view id for this fragment's content (required).
     * @param backStackLabel the label by which to add this fragment to the back stack (nullable).
     * @return the same object as {@code tx}, for method chaining.
     */
    public FragmentTransaction add(FragmentTransaction tx, int containerResId, String backStackLabel) {
        return tx.addToBackStack(backStackLabel)
                .replace(containerResId, this);
    }

    /**
     * Returns the string resource ID to show in the ActionBar Subtitle when navigating to this fragment.
     *
     * @return return value is mandatory; it cannot return 0 for no subtitle.
     */
    public abstract int getTitleResId();

    /**
     * Returns the string resource ID for an accessibility announcement to be spoken when navigating to this fragment.
     *
     * @return defaults to {@code R.string.accessibility__announce_open_page}.
     */
    public int getAnnouncementResId() {
        return R.string.accessibility__announce_open_page;
    }
}
