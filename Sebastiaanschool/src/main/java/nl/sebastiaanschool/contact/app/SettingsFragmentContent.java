package nl.sebastiaanschool.contact.app;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by barend on 6-7-14.
 */
public class SettingsFragmentContent extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        String versionSummary = getString(R.string.settings__version_summary, BuildConfig.VERSION_NAME);
        getPreferenceManager().findPreference("pref_version").setSummary(versionSummary);
        getPreferenceManager().findPreference("pref_github").setOnPreferenceClickListener(this);
    }

    @Override
    public void onStop() {
        new PushPreferencesUpdater(getActivity()).updatePushPreferences();
        super.onStop();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pref_github".equals(preference.getKey())) {
            Uri uri = Uri.parse("https://github.com/sebastiaanschool/sebastiaanschool-android");
            GrabBag.openUri(getActivity(), uri);
            return true;
        }
        return false;
    }
}
