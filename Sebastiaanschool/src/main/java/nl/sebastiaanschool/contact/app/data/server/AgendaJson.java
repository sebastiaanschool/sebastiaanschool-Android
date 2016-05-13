package nl.sebastiaanschool.contact.app.data.server;

/**
 * JSON wire format for the {@code /agendaItems} method. This class is marshalled using Moshi.
 */
public class AgendaJson {
    String type;
    String title;
    String start;
    String end;
    String url;
}
