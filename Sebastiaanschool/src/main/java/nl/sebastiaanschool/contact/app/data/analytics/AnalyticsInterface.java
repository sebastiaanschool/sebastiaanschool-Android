package nl.sebastiaanschool.contact.app.data.analytics;

public interface AnalyticsInterface {
    void navigateToTab(String tabName);

    void itemSelected(String tabName, String itemId, String itemName);
}
