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

import java.util.Date;

/**
 * Created by barend on 6-11-13.
 */
@ParseClassName("Bulletin")
public class Bulletin extends ParseObject {
    private static final String TITLE = "title";
    private static final String BODY = "body";

    public Bulletin() {
        //No-arg constructor for Parse
    }

    public String getTitle() {
        return this.getString(TITLE);
    }

    public String getBody() {
        return this.getString(BODY);
    }

    public long getPublishedAt() {
        Date result = getCreatedAt();
        return result != null ? result.getTime() : 0L;
    }
}
