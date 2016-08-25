package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.sebastiaanschool.contact.app.R;

/**
 * Manages the fragments in our view pager.
 */
public class NavigationPagerAdapter extends FragmentPagerAdapter {

    private final String[] pageTitles;

    private final Fragment[] fragments;

    public NavigationPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        pageTitles = context.getResources().getStringArray(R.array.main__page_titles);
        fragments = new Fragment[pageTitles.length];
        fragments[0] = TimelineFragment.newInstance();
        fragments[1] = AgendaFragment.newInstance();
        fragments[2] = ContactFragment.newInstance();
        fragments[3] = TeamFragment.newInstance();
        fragments[4] = SettingsFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
