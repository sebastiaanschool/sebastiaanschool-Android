package nl.sebastiaanschool.contact.app.data.server;

import org.joda.time.DateTime;

/**
 * Represents items shown on the timeline tab.
 *
 * <p><strong>Implementation note:</strong> this polymorphic implementation is definitely over-
 * engineered. A single class with (or without) an int flag indicating the type would have sufficed.
 * I am aware of that and voluntarily choose to pander to my inner over-engineer. Indulge me.</p>
 */
public abstract class TimelineItem {

    public final String title;
    public final DateTime publishedAt;

    protected TimelineItem(String title, DateTime publishedAt) {
        this.title = title;
        this.publishedAt = publishedAt;
    }

    public static class Bulletin extends TimelineItem {
        public final String body;

        public Bulletin(String title, DateTime publishedAt, String body) {
            super(title, publishedAt);
            this.body = body;
        }
    }

    public static class Newsletter extends TimelineItem {
        public final String documentUrl;

        public Newsletter(String title, DateTime publishedAt, String documentUrl) {
            super(title, publishedAt);
            this.documentUrl = documentUrl;
        }
    }

    public static class Unknown extends TimelineItem {
        public final String type;

        public Unknown(String title, DateTime publishedAt, String type) {
            super(title, publishedAt);
            this.type = type;
        }
    }
}
