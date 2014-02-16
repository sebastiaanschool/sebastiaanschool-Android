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
 * Created by barend on 3-11-13.
 */
@ParseClassName("AgendaItem")
public class AgendaItem extends ParseObject {
    private static final String TITLE = "name";
    private static final String START_TIMESTAMP = "start";
    private static final String END_TIMESTAMP = "end";

    public AgendaItem() {
        // no-arg constructor for Parse.
    }

    public String getTitle() {
        return this.getString(TITLE);
    }

    public long getStartTimestamp() {
        Date start = this.getDate(START_TIMESTAMP);
        return start != null ? start.getTime() : 0L;
    }

    public long getEndTimestamp() {
        Date end = this.getDate(END_TIMESTAMP);
        return end != null ? end.getTime() : Long.MAX_VALUE;
    }

    public boolean hasEndDate() {
        long endTimestamp = getEndTimestamp();
        return endTimestamp != Long.MAX_VALUE && endTimestamp != getStartTimestamp();
    }
}
