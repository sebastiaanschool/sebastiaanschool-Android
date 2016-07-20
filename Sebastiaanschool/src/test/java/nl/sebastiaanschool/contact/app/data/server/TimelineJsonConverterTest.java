package nl.sebastiaanschool.contact.app.data.server;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimelineJsonConverterTest {

    @BeforeClass
    public static void initJodaTime() {
        DateTimeZone.setProvider(new UTCProvider());
    }

    @Test
    public void testBulletinFromJson() throws Exception {
        TimelineJson fromJson = new TimelineJson();
        fromJson.type = "bulletin";
        fromJson.title = "Nieuwsbrief februari 2016";
        fromJson.body = "Er staat weer een nieuwe nieuwsbrief voor u klaar! Veel leesplezier!";
        fromJson.documentUrl = null;
        fromJson.publishedAt = "2016-02-12T07:49:47.883000Z";

        TimelineItem item = new TimelineJsonConverter().fromJson(fromJson);
        assertThat(item.type, is(TimelineItem.TYPE_BULLETIN));
        assertThat(item.title, is("Nieuwsbrief februari 2016"));
        assertThat(item.body, is("Er staat weer een nieuwe nieuwsbrief voor u klaar! Veel leesplezier!"));
        assertThat(item.publishedAt, is(new DateTime(2016, 2, 12, 7, 49, 47, 883, DateTimeZone.UTC)));
    }

    @Test
    public void testNewsletterFromJson() throws Exception {
        TimelineJson fromJson = new TimelineJson();
        fromJson.type = "newsletter";
        fromJson.title = "Maart 2016";
        fromJson.body = null;
        fromJson.documentUrl = "http://www.sebastiaanschool.nl/images/Documenten/pdf/nbmaart2016.pdf";
        fromJson.publishedAt = "2016-03-11T12:07:05Z";

        TimelineItem item = new TimelineJsonConverter().fromJson(fromJson);
        assertThat(item.type, is(TimelineItem.TYPE_NEWSLETTER));
        assertThat(item.title, is("Maart 2016"));
        assertThat(item.documentUrl, is("http://www.sebastiaanschool.nl/images/Documenten/pdf/nbmaart2016.pdf"));
        assertThat(item.publishedAt, is(new DateTime(2016, 3, 11, 12, 7, 5, DateTimeZone.UTC)));
    }

    @Test
    public void testUnknownFromJson() throws Exception {
        TimelineJson fromJson = new TimelineJson();
        fromJson.type = "banana";
        fromJson.title = "This is about bananas";
        fromJson.body = null;
        fromJson.documentUrl = null;
        fromJson.publishedAt = "2016-05-05T12:00:00Z";

        TimelineItem item = new TimelineJsonConverter().fromJson(fromJson);
        assertThat(item.type, is(TimelineItem.TYPE_UNKNOWN));
        assertThat(item.title, is("This is about bananas"));
        assertThat(item.publishedAt, is(new DateTime(2016, 5, 5, 12, 0, 0, DateTimeZone.UTC)));
    }
}
