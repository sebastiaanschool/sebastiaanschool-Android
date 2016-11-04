package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;

/**
 * Manages the fragments in our view pager.
 */
public class NavigationPagerAdapter extends FragmentPagerAdapter {

    /** Page title, for display (localized). */
    private final String[] pageTitles;

    /** Page name, for analytics. */
    private final String[] pageNames;

    private final Fragment[] fragments;
    private final TimelineFragment timelineFragment;

    public NavigationPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        pageNames = new String[]{ "Timeline", "Agenda", "Contact", "Team", "Settings" };
        pageTitles = context.getResources().getStringArray(R.array.main__page_titles);
        fragments = new Fragment[pageTitles.length];
        fragments[0] = timelineFragment = TimelineFragment.newInstance();
        fragments[1] = AgendaFragment.newInstance();
        fragments[2] = ContactFragment.newInstance();
        fragments[3] = TeamFragment.newInstance();
        fragments[4] = SettingsFragment.newInstance();
    }

    public ViewPager.OnPageChangeListener enableAnalytics(AnalyticsInterface analytics) {
        for (int i = 0, max = fragments.length; i < max; i++) {
            Fragment f = fragments[i];
            if (f instanceof AnalyticsCapableFragment) {
                ((AnalyticsCapableFragment) f).enableAnalytics(analytics, pageNames[i]);
            }
        }
        return new AnalyticsPageChangeListener(analytics);
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

    public void onTimelineUpdateNotificationTapped() {
        if (timelineFragment != null) {
            timelineFragment.refreshAndScrollToTop();
        }
    }

    public void onTimelineUpdateBroadcastReceived() {
        if (timelineFragment != null) {
            timelineFragment.onTimelineUpdateBroadcastReceived();
        }
    }

    public boolean isTimelineCurrentItem(ViewPager vp) {
        return vp.getAdapter() == this && vp.getCurrentItem() == 0;
    }

    public void setTimelineAsCurrentItem(ViewPager vp) {
        if (vp.getAdapter() == this) {
            vp.setCurrentItem(0);
        }
    }

    private class AnalyticsPageChangeListener
            extends ViewPager.SimpleOnPageChangeListener {
        private final AnalyticsInterface analytics;

        public AnalyticsPageChangeListener(AnalyticsInterface analytics) {
            this.analytics = analytics;
        }

        @Override
        public void onPageSelected(int position) {
            analytics.navigateToTab(pageNames[position]);
        }
    }
}
