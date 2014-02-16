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
public class TeamMemberView extends LinearLayout {
    private TextView displayName;
    private TextView detailText;

    public TeamMemberView(Context context) {
        super(context);
    }

    public TeamMemberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamMemberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.displayName = (TextView) findViewById(R.id.team__displayName);
        this.detailText = (TextView) findViewById(R.id.team__detailText);

        if (isInEditMode()) {
            this.displayName.setText("Jan Klaassen");
            this.detailText.setText("Poppenkastpop");
        }
    }

    public void setEvent(TeamMember teamMember) {
        this.displayName.setText(teamMember.getDisplayName());
        this.detailText.setText(teamMember.getDetailText());
    }
}
