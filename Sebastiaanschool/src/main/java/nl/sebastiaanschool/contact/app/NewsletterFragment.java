/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Barend on 1-11-13.
 */
public class NewsletterFragment extends SebListFragment<Newsletter> {

    private static final String HTTP_REFERRER = "Referer";
    private String referrer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        try {
            final Uri uri = Uri.parse(item.getUrl());
            final String segment = uri.getLastPathSegment();
            if (segment != null && segment.endsWith(".pdf")) {
                downloadWithDownloadManager(activity, uri);
            } else {
                GrabBag.openUri(activity, uri);
            }
        } catch (Exception e) {
            Log.w("NewsletterFragment", "Exception while downloading " + item.getUrl() + " .", e);
            new AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setTitle(R.string.download_failed)
                    .setMessage(item.getUrl())
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    private void downloadWithDownloadManager(Context context, Uri uri) {
        Analytics.trackEvent("Download " + uri);
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.addRequestHeader(HTTP_REFERRER, constructReferrer(context));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription(getString(R.string.download_newsletter_description));
        request.setTitle(uri.getLastPathSegment());
        dm.enqueue(request);
    }

    private String constructReferrer(Context context) {
        if (referrer == null) {
            int versionCode = -1;
            try {
                versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                // Ignored
            }
            referrer =  context.getString(R.string.download_newsletter_referer, versionCode);
        }
        return referrer;
    }
}
