/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

/**
 * Created by barend on 6-11-13.
 */
public class Bulletin {
    private String title;
    private String body;
    private long createdAt;

    public Bulletin(String title, String body, long createdAt) {
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public long getPublishedAt() {
        return this.getCreatedAt();
    }
}
