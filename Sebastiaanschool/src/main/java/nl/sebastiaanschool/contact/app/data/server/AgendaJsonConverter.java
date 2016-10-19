package nl.sebastiaanschool.contact.app.data.server;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Handles data conversion when parsing the {@code /timeline} API call.
 */
public class AgendaJsonConverter {

    private static final DateTimeFormatter dtp = ISODateTimeFormat.dateTimeParser();

    @ToJson
    @SuppressWarnings("unused")
    public AgendaJson toJson(AgendaItem item) {
        throw new UnsupportedOperationException();
    }

    @FromJson
    public AgendaItem fromJson(AgendaJson item) {
        final DateTime start = item.start != null
                ? dtp.parseDateTime(item.start)
                : new DateTime(2000, 1, 1, 0, 0);
        final DateTime end = item.end != null
                ? dtp.parseDateTime(item.end)
                : new DateTime(2000, 1, 1, 0, 0);
        return new AgendaItem(item.title, start, end, item.url);
    }
}
