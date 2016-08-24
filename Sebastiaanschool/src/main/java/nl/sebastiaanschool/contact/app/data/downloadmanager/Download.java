package nl.sebastiaanschool.contact.app.data.downloadmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Mutable state for presenting download feedback in the UI.
 */
public class Download {
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
     * Download has failed.
     */
    public static final int STATUS_FAILED = 3;

    /**
     * The URL isn't something we download, just launch it in the system browser.
     */
    public static final int STATUS_OPEN_ON_WEB = 4;

    /**
     * The download was cancelled by the user.
     */
    public static final int STATUS_CANCELLED = 5;

    /**
     * File size is unknown.
     */
    public static final long SIZE_UNKNOWN = -1L;

    @NonNull public final String remoteUrl;
    public long sizeInBytes = SIZE_UNKNOWN;
    @StatusCode public int statusCode;
    public int lastStatusCode;
    public long downloadManagerId;
    public String localUri;
    public String localMimeType;

    public Download(@NonNull String url) {
        this.remoteUrl = url;
        this.statusCode = isPdf(url) ? STATUS_PENDING : STATUS_OPEN_ON_WEB;
    }

    public Download withSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    public Download withStatusCode(@StatusCode int code) {
        this.lastStatusCode = this.statusCode;
        this.statusCode = code;
        return this;
    }

    public Download withDownloadManagerId(long id) {
        this.downloadManagerId = id;
        return this;
    }

    public Download withLocalFile(String uri, String mimeType) {
        this.localUri = uri;
        this.localMimeType = mimeType;
        return this;
    }

    public Single<Intent> createOpeningIntent(final Context context) {
        if (!(statusCode == STATUS_COMPLETED || statusCode == STATUS_OPEN_ON_WEB)) {
            return Single.error(new IllegalStateException());
        }
        return Single.create(new Single.OnSubscribe<Intent>() {
            @Override
            public void call(SingleSubscriber<? super Intent> singleSubscriber) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (statusCode == STATUS_COMPLETED) {
                    intent.setDataAndType(Uri.parse(localUri), localMimeType);
                } else if (statusCode == STATUS_OPEN_ON_WEB) {
                    intent.setData(Uri.parse(remoteUrl));
                }
                singleSubscriber.onSuccess(intent);
            }
        });
    }

    @Override
    public String toString() {
        return "Download{" +
                "status=" + status(statusCode) +
                ", downloadManagerId=" + downloadManagerId +
                ", sizeInBytes=" + sizeInBytes +
                ", remoteUrl='" + remoteUrl + '\'' +
                '}';
    }

    private static boolean isPdf(String url) {
        String s = Uri.parse(url).getLastPathSegment();
        return s != null && s.endsWith(".pdf");
    }

    private static String status(@StatusCode int code) {
        switch (code) {
            case STATUS_PENDING:
                return "pending";
            case STATUS_DOWNLOADING:
                return "downloading";
            case STATUS_COMPLETED:
                return "completed";
            case STATUS_FAILED:
                return "failed";
            case STATUS_OPEN_ON_WEB:
                return "open_on_web";
            default:
                return "unknown";
        }
    }

    @IntDef({ STATUS_PENDING, STATUS_DOWNLOADING, STATUS_COMPLETED, STATUS_FAILED,
            STATUS_OPEN_ON_WEB, STATUS_CANCELLED})
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface StatusCode {}
}
