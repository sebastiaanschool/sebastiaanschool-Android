package nl.sebastiaanschool.contact.app.gui;


import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;

public class SettingsFragment extends PreferenceFragmentCompat
        implements AnalyticsCapableFragment {

    private AnalyticsInterface analytics;
    private String analyticsCategory;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getPreferenceManager().setSharedPreferencesName("Sebastiaanschool_prefs");
        addPreferencesFromResource(R.xml.preferences);
        String versionSummary = getString(R.string.settings__version_summary, BuildConfig.VERSION_NAME);
        getPreferenceManager().findPreference("pref_version").setTitle(versionSummary);
        getPreferenceManager().findPreference("push_enabled").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (analytics != null) {
                    String key = preference.getKey();
                    analytics.itemSelected(analyticsCategory, key, key);
                }
                return true;
            }
        });
    }

    @Override
    public void enableAnalytics(AnalyticsInterface analytics, String category) {
        this.analytics = analytics;
        this.analyticsCategory = category;
    }
}
