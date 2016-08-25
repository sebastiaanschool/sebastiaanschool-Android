package nl.sebastiaanschool.contact.app.data.server;

import org.joda.time.DateTime;

/**
 * Represents items shown on the agenda tab.
 */
@SuppressWarnings("WeakerAccess")
public class AgendaItem {

    public final String title;
    public final DateTime start;
    public final DateTime end;

    /** Canonical ID. */
    public final String url;

    public AgendaItem(String title, DateTime start, DateTime end, String url) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.url = url;
    }
}
