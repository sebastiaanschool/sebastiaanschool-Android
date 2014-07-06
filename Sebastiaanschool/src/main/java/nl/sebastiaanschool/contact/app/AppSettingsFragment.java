package nl.sebastiaanschool.contact.app;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

/**
 * Created by barend on 6-7-14.
 */
public class AppSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().findPreference("pref_version").setSummary("Versie " + BuildConfig.VERSION_NAME);
        getPreferenceManager().findPreference("pref_github").setOnPreferenceClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(0xFFFFFFFF);
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
