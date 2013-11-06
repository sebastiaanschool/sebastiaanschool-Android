package nl.sebastiaanschool.contact.app;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.lang.String;

/**
 * Created by barend on 3-11-13.
 */
@ParseClassName("ContactItem")
public class TeamMember extends ParseObject {
    private static final String DISPLAY_NAME = "displayName";
    private static final String DETAIL_TEXT = "detailText";
    private static final String EMAIL = "email";

    public TeamMember() {
        // no-arg constructor for Parse.
    }

    public String getDisplayName() {
        return this.getString(DISPLAY_NAME);
    }

    public String getDetailText() {
        return this.getString(DETAIL_TEXT);
    }

    public String getEmail() {
        return this.getString(EMAIL);
    }
}
