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

/**
 * Displays newsletter download status.
 */
public class DownloadStatusView extends FrameLayout {

    @DrawableRes
    private static final int[] STATUS_ICONS = {
            R.drawable.ic_download_pending_24dp,
            R.drawable.ic_download_in_progress_24dp,
            R.drawable.ic_download_complete_24dp,
            R.drawable.ic_download_failed_24dp
    };

    @StringRes
    private static final int[] CONTENT_DESCRIPTIONS = {
            R.string.download_pending,
            R.string.download_in_progress,
            R.string.download_complete,
            R.string.download_failed
    };

    private ImageView icon;
    private TextView label;

    private DownloadStatus status;

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

    private final void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_download_status, this, true);
        this.icon = (ImageView) findViewById(R.id.vds__icon);
        this.label = (TextView) findViewById(R.id.vds__label);
        label.setVisibility(GONE);
        updateStatusImage(DownloadStatus.STATUS_PENDING);
    }

    public void setStatus(@NonNull DownloadStatus status) {
        if (this.status != status) {
            this.status = status;
            updateStatusImage(status.statusCode);
            updateSizeLabel();
        }
    }

    private void updateSizeLabel() {
        if (status.sizeInBytes == DownloadStatus.SIZE_UNKNOWN) {
            label.setVisibility(GONE);
        } else {
            label.setText(Formatter.formatShortFileSize(getContext(), status.sizeInBytes));
            label.setVisibility(VISIBLE);
        }
    }

    private void updateStatusImage(@DownloadStatus.DownloadStatusCode int statusCode) {
        this.icon.setImageDrawable(GrabBag.loadVectorDrawable(getContext(),
                STATUS_ICONS[statusCode]));
        this.icon.setContentDescription(getContext().getString(CONTENT_DESCRIPTIONS[statusCode]));
    }
}
