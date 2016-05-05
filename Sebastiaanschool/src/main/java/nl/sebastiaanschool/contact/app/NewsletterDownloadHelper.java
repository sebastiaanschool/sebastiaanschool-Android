package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

/**
 * Implements the callback interfaces required for newsletter downloaderation.
 */
public class NewsletterDownloadHelper implements DownloadManagerAsyncTask.Callback {
    private final Context context;

    public NewsletterDownloadHelper(Context context) {
        this.context = context;
    }

    public void downloadNewsletterFromUri(Uri uri) {
        final String segment = uri.getLastPathSegment();
        if (segment != null && segment.endsWith(".pdf")) {
            DownloadManagerAsyncTask newsletterDownloadAsyncTask = new DownloadManagerAsyncTask(context, this);
            newsletterDownloadAsyncTask.execute(new DownloadManagerAsyncTask.Param(uri));
        } else {
            GrabBag.openUri(context, uri);
        }
    }

    @Override
    public void launchDownloadedFile(File file, String mimeType) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), mimeType);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, R.string.open_uri_failed, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
