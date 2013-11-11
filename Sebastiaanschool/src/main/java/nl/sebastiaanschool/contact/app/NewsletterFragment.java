/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Barend on 1-11-13.
 */
public class NewsletterFragment extends SebListFragment<Newsletter> {

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new NewsletterAdapter(context);
    }

    @Override
    protected void onItemClick(Newsletter item) {
        Activity activity = getActivity();
        if (activity != null) {
            GrabBag.openUri(activity, item.getUrl());
        }
    }
}
