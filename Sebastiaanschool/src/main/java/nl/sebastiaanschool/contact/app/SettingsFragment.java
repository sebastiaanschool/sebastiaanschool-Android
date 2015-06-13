package nl.sebastiaanschool.contact.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * HorizontalSlidingFragment wrapper for {@link nl.sebastiaanschool.contact.app.SettingsFragmentContent}.
 * <p>
 * The wrapping is a work-around for the problem of having two base classes for the settings screen:
 * {@code HorizontalSlidingFragment} and {@code PreferenceFragment}. This was probably the cue to
 * refactor HorizontalSlidingFragment into a ViewGroup. I missed it.
 * </p>
 */
public class SettingsFragment extends SebFragment {

    private SettingsFragmentContent contents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        contents = new SettingsFragmentContent();
        getFragmentManager().beginTransaction().add(R.id.settings__content, contents).commit();
        return view;
    }

    @Override
    public void onDestroy() {
        if (contents != null) {
            getFragmentManager().beginTransaction().remove(contents).commit();
            contents = null;
        }
        super.onDestroy();
    }

    @Override
    public int getTitleResId() {
        return R.string.navigation__settings;
    }
}
