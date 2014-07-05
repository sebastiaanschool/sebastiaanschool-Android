/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

/**
 * Created by Barend on 1-11-13.
 */
public class NewsletterFragment extends SebListFragment<Newsletter> {

    private Callback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callback = (Callback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    @Override
    protected SebListAdapter createAdapter(Context context) {
        return new NewsletterAdapter(context);
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__newsletter;
    }

    @Override
    public int getAnnouncementResId() {
        return R.string.accessibility__announce_open_newsletter;
    }

    @Override
    protected void onItemClick(Newsletter item) {
        final Uri uri = Uri.parse(item.getUrl());
        if (callback != null) {
            callback.downloadNewsletterFromUri(uri);
        }
    }

    public static interface Callback {
        void downloadNewsletterFromUri(Uri uri);
    }
}
