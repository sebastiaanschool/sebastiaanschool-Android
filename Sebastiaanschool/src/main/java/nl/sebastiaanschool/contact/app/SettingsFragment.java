package nl.sebastiaanschool.contact.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Application attribution and settings.
 */
public class SettingsFragment extends PreferenceFragment implements SebFragment {


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        String versionSummary = getString(R.string.settings__version_summary, BuildConfig.VERSION_NAME);
        getPreferenceManager().findPreference("pref_version").setSummary(versionSummary);
    }

    @Override
    public void onStop() {
        new PushPreferencesUpdater(getActivity()).updatePushPreferences();
        super.onStop();
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__settings;
    }
}
