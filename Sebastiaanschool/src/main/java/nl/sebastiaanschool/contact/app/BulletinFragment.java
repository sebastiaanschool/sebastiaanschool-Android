/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;

/**
 * Created by Barend on 1-11-13.
 */
public class BulletinFragment extends SebListFragment<Bulletin> {

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new BulletinAdapter(context);
    }

    @Override
    protected void onItemClick(Bulletin item) {
        // Nothing
    }
}
