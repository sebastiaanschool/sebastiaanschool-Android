package nl.sebastiaanschool.contact.app.gui;

import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;

interface AnalyticsCapableFragment {
    void enableAnalytics(AnalyticsInterface analytics, String category);
}
