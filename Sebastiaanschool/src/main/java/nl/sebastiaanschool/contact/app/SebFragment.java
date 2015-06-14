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
public interface SebFragment {

    /**
     * Returns the string resource ID to show as activity title when navigating to this fragment.
     *
     * @return return value is mandatory; it cannot return 0 for no subtitle.
     */
    int getTitleResId();
}
