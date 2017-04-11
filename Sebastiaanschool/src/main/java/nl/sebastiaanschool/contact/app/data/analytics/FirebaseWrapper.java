package nl.sebastiaanschool.contact.app.data.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.data.GrabBag;

public class FirebaseWrapper implements AnalyticsInterface {

    private final FirebaseAnalytics firebase;

    public static FirebaseWrapper init(Context context) {
        return new FirebaseWrapper(context);
    }

    private FirebaseWrapper(Context context) {
        this.firebase = FirebaseAnalytics.getInstance(context);
        this.firebase.setUserProperty("has_debug_build", BuildConfig.DEBUG ? "true" : "false");
        this.firebase.setUserProperty("locale", GrabBag.getResourcesLocale(context).toString());
    }

    @Override
    public void navigateToTab(String tabName) {
        Bundle b = new Bundle();
        b.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, tabName);
        this.firebase.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, b);
    }

    @Override
    public void itemSelected(String tabName, String itemId, String itemName) {
        Bundle b = new Bundle();
        b.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, tabName);
        b.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        b.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        this.firebase.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, b);
    }
}
