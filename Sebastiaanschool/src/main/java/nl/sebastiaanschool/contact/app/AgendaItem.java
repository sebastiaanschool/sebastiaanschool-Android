package nl.sebastiaanschool.contact.app;

import android.os.Build;
import android.text.format.DateFormat;

import java.util.Locale;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaItem {
    private static final long NO_END_TIME = Long.MAX_VALUE;
    public static final String DATE_PATTERN = getDatePattern();
    private final String title;
    private final long startTimestamp;
    private final long endTimestamp;

    public AgendaItem(String title, long startTimestamp) {
        this.title = title;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = NO_END_TIME;
    }

    public AgendaItem(String title, long startTimestamp, long endTimestamp) {
        this.title = title;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public boolean hasEndDate() {
        return endTimestamp != NO_END_TIME && endTimestamp > startTimestamp;
    }

    public CharSequence getDatesFormatted() {
        StringBuilder result = new StringBuilder(32);
        result.append(DateFormat.format(DATE_PATTERN, startTimestamp));
        if (hasEndDate()) {
            result.append(" – ");
            result.append(DateFormat.format(DATE_PATTERN, endTimestamp));
        }
        return result;
    }

    private static String getDatePattern() {
        if (Build.VERSION.SDK_INT >= 18) {
            return DateFormat.getBestDateTimePattern(Locale.getDefault(), "dMMMMyyyy");
        } else if ("nl".equals(Locale.getDefault().getLanguage())) {
            return "d MMMM yyyy";
        } else {
            return "MMMM d, yyyy";
        }
    }
}
