package nl.sebastiaanschool.contact.app.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.BuildConfig;
import nl.sebastiaanschool.contact.app.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        String versionSummary = getString(R.string.settings__version_summary, BuildConfig.VERSION_NAME);
        getPreferenceManager().findPreference("pref_version").setSummary(versionSummary);
    }
}
