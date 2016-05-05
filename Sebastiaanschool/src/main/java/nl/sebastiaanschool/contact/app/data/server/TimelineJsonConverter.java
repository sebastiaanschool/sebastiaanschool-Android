package nl.sebastiaanschool.contact.app.data.server;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Handles polymorphism when parsing the {@code /timeline} API call.
 */
public class TimelineJsonConverter {

    private static final DateTimeFormatter dtp = ISODateTimeFormat.dateTimeParser();

    @ToJson
    @SuppressWarnings("unused")
    public TimelineJson toJson(TimelineItem item) {
        throw new UnsupportedOperationException();
    }

    @FromJson
    public TimelineItem fromJson(TimelineJson item) {
        final TimelineItem result;
        final DateTime published = item.publishedAt != null
                ? dtp.parseDateTime(item.publishedAt)
                : new DateTime(2000, 1, 1, 0, 0);
        if ("newsletter".equals(item.type)) {
            result = new TimelineItem.Newsletter(item.title, published, item.documentUrl);
        } else if ("bulletin".equals(item.type)) {
            result = new TimelineItem.Bulletin(item.title, published, item.body);
        } else {
            result = new TimelineItem.Unknown(item.title, published, item.type);
        }
        return result;
    }
}
