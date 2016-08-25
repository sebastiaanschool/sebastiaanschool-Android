package nl.sebastiaanschool.contact.app.data;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Locale;

import nl.sebastiaanschool.contact.app.BuildConfig;

class GrabBag {
    /**
     * Creates a user-agent string that should generally be RFC compliant. If the device constants
     * contain context-invalid characters these are used as-is and RFC compliance will suffer.
     * @param context used to obtain some device data.
     * @return e.g. "Sebastiaanschool/2.0.0 (LGE; Nexus 5; Android 6.1 SDK 23; nl_NL)"
     */
    static String constructUserAgent(@NonNull Context context) {
        return  new StringBuilder(256)
                .append("Sebastiaanschool/").append(BuildConfig.VERSION_NAME)
                .append(" (").append(Build.MANUFACTURER).append("; ").append(Build.MODEL)
                .append("; Android ").append(Build.VERSION.RELEASE)
                .append(" SDK ").append(Build.VERSION.SDK_INT)
                .append("; ").append(getResourcesLocale(context))
                .append(')')
                .toString();
    }

    private static Locale getResourcesLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
