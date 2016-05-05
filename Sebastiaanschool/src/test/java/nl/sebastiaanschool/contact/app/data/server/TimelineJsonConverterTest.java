package nl.sebastiaanschool.contact.app.data.server;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimelineJsonConverterTest {

    @Test
    public void testBulletinFromJson() throws Exception {
        TimelineJson fromJson = new TimelineJson();
        fromJson.type = "bulletin";
        fromJson.title = "Nieuwsbrief februari 2016";
        fromJson.body = "Er staat weer een nieuwe nieuwsbrief voor u klaar! Veel leesplezier!";
        fromJson.documentUrl = null;
        fromJson.publishedAt = "2016-02-12T07:49:47.883000Z";

        TimelineItem item = new TimelineJsonConverter().fromJson(fromJson);
        assertThat(item, is(instanceOf(TimelineItem.Bulletin.class)));

        TimelineItem.Bulletin bull = (TimelineItem.Bulletin) item;
        assertThat(bull.title, is("Nieuwsbrief februari 2016"));
        assertThat(bull.body, is("Er staat weer een nieuwe nieuwsbrief voor u klaar! Veel leesplezier!"));
        assertThat(bull.publishedAt, is(new DateTime(2016, 2, 12, 7, 49, 47, 883, DateTimeZone.UTC)));
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
        assertThat(item, is(instanceOf(TimelineItem.Newsletter.class)));

        TimelineItem.Newsletter news = (TimelineItem.Newsletter) item;
        assertThat(news.title, is("Maart 2016"));
        assertThat(news.documentUrl, is("http://www.sebastiaanschool.nl/images/Documenten/pdf/nbmaart2016.pdf"));
        assertThat(news.publishedAt, is(new DateTime(2016, 3, 11, 12, 7, 5, DateTimeZone.UTC)));
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
        assertThat(item, is(instanceOf(TimelineItem.Unknown.class)));

        TimelineItem.Unknown news = (TimelineItem.Unknown) item;
        assertThat(news.title, is("This is about bananas"));
        assertThat(news.type, is("banana"));
        assertThat(news.publishedAt, is(new DateTime(2016, 5, 5, 12, 0, 0, DateTimeZone.UTC)));
    }
}
