/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by barend on 3-11-13.
 */
public class NewsletterView extends LinearLayout {
    private TextView itemName;
    private TextView itemDate;

    public NewsletterView(Context context) {
        super(context);
    }

    public NewsletterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsletterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.itemName = (TextView) findViewById(R.id.newsletter__item_name);
        this.itemDate = (TextView) findViewById(R.id.newsletter__item_date);

        if (isInEditMode()) {
            this.itemName.setText("Kerstnieuwsbrief");
            this.itemDate.setText("23 december 2013");
        }
    }

    public void setEvent(Newsletter event) {
        this.itemName.setText(event.getName());
        this.itemDate.setText(GrabBag.formatDate(event.getPublishedAt()));
    }
}
