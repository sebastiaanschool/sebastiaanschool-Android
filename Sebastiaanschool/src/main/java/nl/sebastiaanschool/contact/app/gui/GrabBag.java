/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

import nl.sebastiaanschool.contact.app.R;

/**
 * This class is normally called {@code SomethingUtility}, but that's boring.
 * Created by barend on 6-11-13.
 */
public final class GrabBag {

    public static void openUri(Context context, String uriString) {
        openUri(context, Uri.parse(uriString));
    }

    public static void openUri(Context context, Uri uri) {
        Intent browse = new Intent(Intent.ACTION_VIEW, uri);
        browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Need to check if a browser is present, as it can be disabled entirely using child safety features on a tablet.
        List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(browse, 0);
        if (!handlers.isEmpty()) {
            context.startActivity(browse);
        } else {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle(R.string.open_uri_failed)
                    .setMessage(uri.toString())
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    private GrabBag() {}
}
