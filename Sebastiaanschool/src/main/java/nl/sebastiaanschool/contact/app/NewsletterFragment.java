/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Barend on 1-11-13.
 */
public class NewsletterFragment extends SebListFragment<Newsletter> {

    private Callback callback;

    public static NewsletterFragment newInstance() {
        return new NewsletterFragment();
    }

    public NewsletterFragment() {
        setHasOptionsMenu(true);
    }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.newsletter_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_downloads_folder) {
            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
