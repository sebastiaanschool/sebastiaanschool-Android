package nl.sebastiaanschool.contact.app.gui;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.downloadmanager.Download;

/**
 * Displays newsletter download status.
 */
public class DownloadStatusView extends FrameLayout {

    @DrawableRes
    private static final int[] STATUS_ICONS = {
            R.drawable.ic_download_pending_24dp,     // Pending
            R.drawable.ic_download_in_progress_24dp, // In progress
            R.drawable.ic_download_launch_24dp,      // Completed
            R.drawable.ic_download_failed_24dp,      // Failed
            R.drawable.ic_download_launch_24dp,      // Open on web
            R.drawable.ic_download_cancelled_24dp,   // Cancelled
    };

    @StringRes
    private static final int[] CONTENT_DESCRIPTIONS = {
            R.string.download_pending,
            R.string.download_in_progress,
            R.string.download_complete,
            R.string.download_failed,
            R.string.download_complete,
            R.string.download_cancelled,
    };

    private ImageView icon;
    private TextView label;

    private Download status;

    public DownloadStatusView(Context context) {
        super(context);
        init(context);
    }

    public DownloadStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DownloadStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public DownloadStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_download_status, this, true);
        this.icon = (ImageView) findViewById(R.id.vds__icon);
        this.label = (TextView) findViewById(R.id.vds__label);
        label.setVisibility(GONE);
        updateStatusImage(Download.STATUS_PENDING);
    }

    public void setStatus(@NonNull Download status) {
        this.status = status;
        updateStatusImage(status.statusCode);
        updateSizeLabel();
    }

    private void updateSizeLabel() {
        if (status.sizeInBytes == Download.SIZE_UNKNOWN
                || status.statusCode == Download.STATUS_COMPLETED) {
            label.setVisibility(GONE);
        } else {
            label.setText(Formatter.formatShortFileSize(getContext(), status.sizeInBytes));
            label.setVisibility(VISIBLE);
        }
    }

    private void updateStatusImage(@Download.StatusCode int statusCode) {
        GrabBag.applyVectorImage(this.icon, STATUS_ICONS[statusCode]);
        this.icon.setContentDescription(getContext().getString(CONTENT_DESCRIPTIONS[statusCode]));
    }
}
