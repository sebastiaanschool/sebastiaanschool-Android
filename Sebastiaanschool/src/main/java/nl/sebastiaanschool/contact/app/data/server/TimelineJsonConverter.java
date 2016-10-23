package nl.sebastiaanschool.contact.app.data.server;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Handles data conversion when parsing the {@code /timeline} API call.
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
            result = TimelineItem.newsletter(item.url, item.title, item.documentUrl, published);
        } else if ("bulletin".equals(item.type)) {
            result = TimelineItem.bulletin(item.url, item.title, item.body, published);
        } else {
            result = TimelineItem.unknown(item.url, item.title, published);
        }
        return result;
    }
}
