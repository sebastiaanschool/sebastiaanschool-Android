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
@ParseClassName("NewsLetter")
public class Newsletter extends ParseObject {
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String PUBLISHED_AT = "publishedAt";

    public Newsletter() {
        //No-arg constructor for Parse
    }

    public String getName() {
        return this.getString(NAME);
    }

    public String getUrl() {
        return this.getString(URL);
    }

    public long getPublishedAt() {
        Date date = this.getDate(PUBLISHED_AT);
        return date != null ? date.getTime() : 0;
    }
}
