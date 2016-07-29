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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.util.LruCache;
import android.widget.TextView;

import java.util.List;

import nl.sebastiaanschool.contact.app.R;

/**
 * This class is normally called {@code SomethingUtility}, but that's boring.
 * Created by barend on 6-11-13.
 */
final class GrabBag {

    public static void openUri(Context context, String uriString) {
        openUri(context, Uri.parse(uriString));
    }

    public static void openUri(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openUri(context, intent);
    }

    public static void openUri(Context context, Intent intent) {
        // Need to check if a browser is present, as it can be disabled entirely using child safety features on a tablet.
        List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!handlers.isEmpty()) {
            context.startActivity(intent);
        } else {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle(R.string.open_uri_failed)
                    .setMessage(intent.getData().toString())
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }

    public static void applyBitmapDrawableLeft(TextView view, @DrawableRes int resId) {
        // This could be defined in XML just as easily, but it's here for symmetry at the call site.
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    /**
     * Apply a vector drawable, using backwards compatibility as needed. This throws an exception
     * if your {@code resId} is actually a bitmap drawable.
     */
    public static void applyVectorDrawableLeft(TextView view, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0);
            view.getCompoundDrawablesRelative()[0].setTint(view.getResources()
                    .getColor(R.color.sebastiaan_blue));
        } else {
            final Context context = view.getContext();
            final Drawable drawable = loadVectorDrawable(context, resId);
            if (Build.VERSION.SDK_INT >= 17) {
                view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        }
    }

    private static final LruCache<Integer, Drawable> vectorDrawableCache = new LruCache<>(32);

    public static Drawable loadVectorDrawable(Context context, @DrawableRes int resId) {
        Drawable result = vectorDrawableCache.get(resId);
        if (result == null) {
            result = VectorDrawableCompat.create(context.getResources(), resId, context.getTheme());
            vectorDrawableCache.put(resId, result);
        }
        return result;
    }

    private GrabBag() {}
}
