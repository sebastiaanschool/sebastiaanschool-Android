package nl.sebastiaanschool.contact.app.data;

import android.content.Context;
import android.os.Build;

import nl.sebastiaanschool.contact.app.BuildConfig;

class GrabBag {
    /**
     * Creates a user-agent string that should generally be RFC compliant. If the device constants
     * contain context-invalid characters these are used as-is and RFC compliance will suffer.
     * @param context used to obtain some device data.
     * @return e.g. "Sebastiaanschool/2.0.0 (LGE; Nexus 5; Android 6.1 SDK 23; nl_NL)"
     */
    static String constructUserAgent(Context context) {
        return  new StringBuilder(256)
                .append("Sebastiaanschool/").append(BuildConfig.VERSION_NAME)
                .append(" (").append(Build.MANUFACTURER).append("; ").append(Build.MODEL)
                .append("; Android ").append(Build.VERSION.RELEASE)
                .append(" SDK ").append(Build.VERSION.SDK_INT)
                .append("; ").append(context.getResources().getConfiguration().locale)
                .append(')')
                .toString();
    }
}
