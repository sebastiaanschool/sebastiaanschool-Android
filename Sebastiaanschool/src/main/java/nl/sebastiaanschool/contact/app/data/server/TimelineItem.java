package nl.sebastiaanschool.contact.app.data.server;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents items shown on the timeline tab.
 */
public class TimelineItem {

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_BULLETIN = 1;
    public static final int TYPE_NEWSLETTER = 2;

    @TimelineItemType
    public final int type;
    @NonNull
    public final String title;
    @Nullable
    public final String body;
    @Nullable
    public final String documentUrl;
    @NonNull
    public final DateTime publishedAt;

    public static TimelineItem bulletin(
            @NonNull String title,
            @NonNull String body,
            @NonNull DateTime publishedAt) {
        return new TimelineItem(TYPE_BULLETIN, title, body, null, publishedAt);
    }

    public static TimelineItem newsletter(
            @NonNull String title,
            @NonNull String documentUrl,
            @NonNull DateTime publishedAt) {
        return new TimelineItem(TYPE_NEWSLETTER, title, null, documentUrl, publishedAt);
    }

    public static TimelineItem unknown(
            @NonNull String title,
            @NonNull DateTime publishedAt) {
        return new TimelineItem(TYPE_UNKNOWN, title, null, null, publishedAt);
    }

    private TimelineItem(
            @TimelineItemType int type,
            @NonNull String title,
            @Nullable String body,
            @Nullable String documentUrl,
            @NonNull DateTime publishedAt) {
        this.type = type;
        this.title = title;
        this.body = body;
        this.documentUrl = documentUrl;
        this.publishedAt = publishedAt;
    }

    @IntDef({TYPE_BULLETIN, TYPE_NEWSLETTER, TYPE_UNKNOWN})
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface TimelineItemType {
    }
}
