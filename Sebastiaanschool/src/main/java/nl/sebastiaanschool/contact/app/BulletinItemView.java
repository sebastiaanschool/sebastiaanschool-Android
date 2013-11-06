package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by barend on 3-11-13.
 */
public class BulletinItemView extends LinearLayout {
    private TextView itemName;
    private TextView itemDate;
    private TextView itemText;

    public BulletinItemView(Context context) {
        super(context);
    }

    public BulletinItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BulletinItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.itemName = (TextView) findViewById(R.id.bulletin__item_title);
        this.itemDate = (TextView) findViewById(R.id.bulletin__item_date);
        this.itemText = (TextView) findViewById(R.id.bulletin__item_text);

        if (isInEditMode()) {
            this.itemName.setText("Kerstnieuwsbrief");
            this.itemDate.setText("23 december 2013");
            this.itemText.setText("In deze nieuwsbrief staat echt ontzettend veel tekst die allemaal over kerstmis gaat en niet meer op één regel past.");
        }
    }

    public void setBulletin(Bulletin event) {
        this.itemName.setText(event.getTitle());
        this.itemDate.setText(GrabBag.formatDate(event.getPublishedAt()));
        this.itemText.setText(event.getBody());
    }
}
