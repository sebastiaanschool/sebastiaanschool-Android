package nl.sebastiaanschool.contact.app.gui;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mutable state for presenting download feedback in the UI.
 */
public class DownloadStatus {
    /**
     * Download hasn't started yet.
     */
    public static final int STATUS_PENDING = 0;

    /**
     * Download is in progress.
     */
    public static final int STATUS_DOWNLOADING = 1;

    /**
     * Download has completed, file is available offline.
     */
    public static final int STATUS_COMPLETED = 2;

    /**
     * File size is unknown.
     */
    public static final long SIZE_UNKNOWN = -1L;

    public long sizeInBytes = SIZE_UNKNOWN;

    @DownloadStatusCode
    public int statusCode;

    @IntDef({ STATUS_PENDING, STATUS_DOWNLOADING, STATUS_COMPLETED})
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface DownloadStatusCode {}
}
