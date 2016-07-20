package nl.sebastiaanschool.contact.app.gui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import nl.sebastiaanschool.contact.app.data.server.TimelineItem;

/**
 * Holds the contents of a {@link TimelineItem} along with mutable state of the download manager.
 */
public class TimelineItemViewModel {

    @TimelineItem.TimelineItemType
    public final int type;
    @NonNull
    public final String title;
    @Nullable
    public final String body;
    @Nullable
    public final String documentUrl;
    @NonNull
    public final DateTime publishedAt;
    @NonNull
    private DownloadStatus downloadStatus;

    public TimelineItemViewModel(@NonNull TimelineItem item) {
        this.type = item.type;
        this.title = item.title;
        this.body = item.body;
        this.documentUrl = item.documentUrl;
        this.publishedAt = item.publishedAt;
        this.downloadStatus = new DownloadStatus();
    }
}
