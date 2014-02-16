/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
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
