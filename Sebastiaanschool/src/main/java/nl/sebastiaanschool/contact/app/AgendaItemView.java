package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaItemView extends LinearLayout {
    private static final String DATE_PATTERN = getDatePattern();
    private TextView eventTitle;
    private TextView eventDate;

    public AgendaItemView(Context context) {
        super(context);
    }

    public AgendaItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgendaItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.eventTitle = (TextView) findViewById(R.id.agenda__event_title);
        this.eventDate = (TextView) findViewById(R.id.agenda__event_date);

        if (isInEditMode()) {
            this.eventTitle.setText("Kerstvakantie 2013");
            this.eventDate.setText("23 december 2013 – 3 januari 2014");
        }
    }

    public void setEvent(AgendaItem event) {
        this.eventTitle.setText(event.getTitle());
        this.eventDate.setText(formatDates(event));
    }

    private static CharSequence formatDates(AgendaItem event) {
        StringBuilder result = new StringBuilder(32);

        result.append(DateFormat.format(DATE_PATTERN, event.getStartTimestamp()));
        if (event.hasEndDate()) {
            result.append(" – ");
            result.append(DateFormat.format(DATE_PATTERN, event.getEndTimestamp()));
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
