package nl.sebastiaanschool.contact.app.data.server;

/**
 * JSON wire format for the {@code /timeline} method. This class is marshalled using Moshi.
 */
public class TimelineJson {
    String type;
    String title;
    String body;
    String documentUrl;
    String publishedAt;
}
