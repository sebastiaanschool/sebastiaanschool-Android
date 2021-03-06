package nl.sebastiaanschool.contact.app.data.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.Log;

import com.jakewharton.rxrelay.PublishRelay;

import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Callable;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.GrabBag;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

/**
 * Implements our download manager interactions.
 */
public class DownloadManagerInterface {

    private static DownloadManagerInterface instance;

    private final DownloadManager downloadManager;
    private final Scheduler downloadManagerAccessor = Schedulers.io();
    private final PublishRelay<DownloadEvent> updates = PublishRelay.create();
    private final String userAgent;
    private final String downloadDescription;

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new DownloadManagerInterface(context.getApplicationContext());
            instance.registerReceiver(context);
        }
    }

    public static synchronized DownloadManagerInterface getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialised.");
        }
        return instance;
    }

    private DownloadManagerInterface(Context context) {
        this.userAgent = GrabBag.constructUserAgent(context);
        this.downloadDescription = context.getString(R.string.download_newsletter_description);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(downloadCompletionReceiver, filter);
    }

    public void destroy(Context context) {
        context.unregisterReceiver(downloadCompletionReceiver);
        instance = null;
    }

    /**
     * Returns a hot observable that emits status updates for downloaded files.
     * @return download observable.
     */
    public Observable<DownloadEvent> statusUpdates() {
        return updates;
    }

    /**
     * Listens for the Download service to tell us one of our downloads finished, then launches the file.
     */
    private final BroadcastReceiver downloadCompletionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                case DownloadManager.ACTION_NOTIFICATION_CLICKED:
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                    updates.call(new DownloadEvent(action, downloadId));
                    break;
            }
        }
    };

    /**
     * Find an existing download for the given URL.
     * @param download a download.
     * @return an observable that yields either the download status, a
     *      {@link java.io.FileNotFoundException} if no download for the given URL is known, or some
     *      other exception if an error occured.
     */
    public Single<Download> findExistingEntry(@NonNull final Download download) {
        return Single.fromCallable(new Callable<Download>() {
            @Override
            public Download call() throws Exception {
                Log.i("DM", "Find exiting entry: id" + download.downloadManagerId);
                DownloadManager.Query query = new DownloadManager.Query();
                if (download.downloadManagerId != 0) {
                    query.setFilterById(download.downloadManagerId);
                }
                Cursor cursor = downloadManager.query(query);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            String remoteUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                            if (download.remoteUrl.equals(remoteUri)) {
                                long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                                long sizeInBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                String mediaType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                                int statusCode;
                                int dmStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                                switch (dmStatus) {
                                    case DownloadManager.STATUS_SUCCESSFUL:
                                        statusCode = Download.STATUS_COMPLETED;
                                        break;
                                    case DownloadManager.STATUS_FAILED:
                                        statusCode = Download.STATUS_FAILED;
                                        break;
                                    case DownloadManager.STATUS_PAUSED:
                                        // fall-through
                                    case DownloadManager.STATUS_RUNNING:
                                        statusCode = Download.STATUS_DOWNLOADING;
                                        break;
                                    default:
                                        statusCode = Download.STATUS_PENDING;
                                }
                                Log.i("DM", "Find exiting entry: id" + download.downloadManagerId + " dmstatus=" + dmStatus + " ourstatus=" + statusCode);
                                return download.withDownloadManagerId(id)
                                               .withStatusCode(statusCode)
                                               .withLocalFile(localUri, mediaType)
                                               .withSizeInBytes(sizeInBytes);
                            }
                            cursor.moveToNext();
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                throw new FileNotFoundException();
            }
        }).subscribeOn(downloadManagerAccessor);
    }

    /**
     * Enqueues the given URL for downloading. Status events for this download will be emitted via
     * the {@link #statusUpdates()} observable.
     *
     * @param download a download.
     */
    public Single<Download> enqueueDownload(@NonNull final Download download) {
        return Single.fromCallable(new Callable<Download>() {
            @Override
            public Download call() throws Exception {
                Uri uri = Uri.parse(download.remoteUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.addRequestHeader("User-Agent", userAgent);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setDescription(downloadDescription);
                request.setTitle(uri.getLastPathSegment());
                long downloadId = downloadManager.enqueue(request);
                return download.withDownloadManagerId(downloadId)
                                .withStatusCode(Download.STATUS_DOWNLOADING);
            }
        }).subscribeOn(downloadManagerAccessor);
    }

    /**
     * Remove a download and delete the local file.
     * @param download a download.
     * @return a cold observable for the removal.
     */
    public Single<Download> remove(@NonNull final Download download) {
        return Single.fromCallable(new Callable<Download>() {
            @Override
            public Download call() throws Exception {
                int numRemoved = downloadManager.remove(download.downloadManagerId);
                if (numRemoved > 0) {
                    return download.withStatusCode(Download.STATUS_PENDING);
                } else {
                    throw new FileNotFoundException();
                }
            }
        }).subscribeOn(downloadManagerAccessor);
    }

    /**
     * Emitted in response to DownloadManager broadcast events.
     */
    public static class DownloadEvent {
        @Type
        public final String type;
        public final long downloadManagerId;

        DownloadEvent(String type, long downloadManagerId) {
            this.type = type;
            this.downloadManagerId = downloadManagerId;
        }

        @Retention(RetentionPolicy.CLASS)
        @StringDef({DownloadManager.ACTION_DOWNLOAD_COMPLETE,
                DownloadManager.ACTION_NOTIFICATION_CLICKED})
        @interface Type {}
    }
}
