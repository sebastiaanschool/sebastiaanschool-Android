package nl.sebastiaanschool.contact.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by barend on 6-7-14.
 */
public class SettingsFragmentContent extends PreferenceFragment {
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
}
