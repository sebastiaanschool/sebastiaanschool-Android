/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;

/**
 * Created by Barend on 1-11-13.
 */
public class BulletinFragment extends SebListFragment<Bulletin> {

    public static BulletinFragment newInstance() {
        return new BulletinFragment();
    }

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new BulletinAdapter(context);
    }

    @Override
    protected void onItemClick(Bulletin item) {
        // Nothing
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__bulletin;
    }
}
