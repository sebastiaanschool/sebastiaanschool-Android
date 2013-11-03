package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by barend on 3-11-13.
 */
public class AgendaItemView extends LinearLayout {
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
            setEvent(new AgendaItem("Kerstvakantie 2013", 1387756800000L, 1388707200000L));
        }
    }

    public void setEvent(AgendaItem event) {
        this.eventTitle.setText(event.getTitle());
        this.eventDate.setText(event.getDatesFormatted());
    }
}
