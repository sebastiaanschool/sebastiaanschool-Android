package nl.sebastiaanschool.contact.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;

import java.util.List;
import java.util.Locale;

/**
 * This class is normally called {@code SomethingUtility}, but that's boring.
 * Created by barend on 6-11-13.
 */
public final class GrabBag {
    private static final String DATE_PATTERN = getDatePattern();

    public static CharSequence formatDate(long timestamp) {
        return DateFormat.format(DATE_PATTERN, timestamp);
    }

    public static void openUri(Context context, String uriString) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Need to check if a browser is present, as it can be disabled entirely using child safety features on a tablet.
        List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(browse, 0);
        if (!handlers.isEmpty()) {
            context.startActivity(browse);
        } else {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setMessage(uriString)
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    private static String getDatePattern() {
        if (Build.VERSION.SDK_INT >= 18) {
            return DateFormat.getBestDateTimePattern(Locale.getDefault(), "dMMMMyyyy");
        } else if ("nl".equals(Locale.getDefault().getLanguage())) {
            return "d MMMM yyyy";
        } else {
            return "MMMM d, yyyy";
        }
    }

    private GrabBag() {}
}