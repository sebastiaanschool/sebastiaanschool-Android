package nl.sebastiaanschool.contact.app;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;

/**
 * Background task to query the DownloadManager database for newsletters.
 * <p/>
 * Given an URL, checks the download manager to see if that URL has already been downloaded. If it
 * has, launches it immediately. Otherwise, schedules the download then launches it. Given a
 * downloadId, launches it immediately.
 */
public class DownloadManagerAsyncTask extends AsyncTask<DownloadManagerAsyncTask.Param, Void, DownloadManagerAsyncTask.Result> {

    private final Context context;
    private final Callback callback;

    public DownloadManagerAsyncTask(final Context context, final Callback callback) {
        this.context = context.getApplicationContext();
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.referrer = constructReferrer(context);
        this.callback = callback;
    }

    private static final String HTTP_REFERRER = "Referer";
    private String referrer;
    private DownloadManager downloadManager;

    @Override
    protected Result doInBackground(Param... params) {
        // First check if the remote URI has already been downloaded, if so, it'll launch the downloaded file
        Result result = checkForExistingFile(params[0]);
        if (result == null) {
            Uri uri = params[0].uri;
            Analytics.trackEvent("Download " + uri);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.addRequestHeader(HTTP_REFERRER, referrer);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDescription(context.getString(R.string.download_newsletter_description));
            request.setTitle(uri.getLastPathSegment());
            long downloadId = downloadManager.enqueue(request);
            result = new Result(downloadId);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result.isFoundInStorage()) {
            callback.launchDownloadedFile(result.localName, result.mimeType);
        } else if (result.isDownloadStarted()) {
            Toast toast = Toast.makeText(context, R.string.download_started, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Nullable
    private Result checkForExistingFile(@NonNull Param params) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        boolean byId = params.downloadId != -1L;
        final String uriString;
        if (byId) {
            query.setFilterById(params.downloadId);
            uriString = null;
        } else {
            uriString = params.uri.toString();
        }
        Cursor cursor = downloadManager.query(query);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String remoteUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                    if (byId || uriString.equals(remoteUri)) {
                        String mediaType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                        String localName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        return new Result(new File(localName), mediaType);
                    }
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private String constructReferrer(Context context) {
        if (referrer == null) {
            int versionCode = -1;
            try {
                versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                // Ignored
            }
            referrer = context.getString(R.string.download_newsletter_referer, versionCode);
        }
        return referrer;
    }

    public static interface Callback {
        void launchDownloadedFile(final File file, final String mimeType);
    }

    public static class Param {
        final long downloadId;
        final Uri uri;

        public Param(long downloadId) {
            this.downloadId = downloadId;
            this.uri = null;
        }

        public Param(Uri uri) {
            this.downloadId = -1L;
            this.uri = uri;
        }
    }

    static class Result {
        final long downloadId;
        final File localName;
        final String mimeType;

        public Result(long downloadId) {
            this.downloadId = downloadId;
            this.localName = null;
            this.mimeType = null;
        }

        public Result(File localName, String mimeType) {
            this.downloadId = -1L;
            this.localName = localName;
            this.mimeType = mimeType;
        }

        public boolean isFoundInStorage() {
            return localName != null;
        }

        public boolean isDownloadStarted() {
            return !isFoundInStorage();
        }
    }
}
