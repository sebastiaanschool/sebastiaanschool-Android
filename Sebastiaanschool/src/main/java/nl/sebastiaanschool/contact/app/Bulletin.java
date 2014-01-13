/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
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
