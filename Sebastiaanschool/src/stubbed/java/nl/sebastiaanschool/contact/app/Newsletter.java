package nl.sebastiaanschool.contact.app;

/**
 * Created by barend on 6-11-13.
 */
public class Newsletter {
    private String name;
    private String url;
    private long publishedAt;

    public Newsletter(String name, String url, long publishedAt) {
        this.name = name;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public long getPublishedAt() {
        return this.publishedAt;
    }
}
