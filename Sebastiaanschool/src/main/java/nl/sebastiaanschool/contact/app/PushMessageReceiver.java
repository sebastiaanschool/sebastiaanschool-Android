package nl.sebastiaanschool.contact.app;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Tailors the parse push message receiver to our needs.
 */
public class PushMessageReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected Notification getNotification(Context context, Intent intent) {
        final Notification notification = super.getNotification(context, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            notification.category = Notification.CATEGORY_EMAIL;
            notification.color = context.getResources().getColor(R.color.sebastiaan_blue);
            notification.visibility = Notification.VISIBILITY_PUBLIC;
        }
        return notification;
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        try {
            int mode = -1;
            final String channel = intent.getStringExtra("com.parse.Channel");
            if (PushPreferencesUpdater.CHANNEL_BULLETIN.equals(channel)) {
                mode = DetailActivity.MODE_BULLETIN;
            } else if (PushPreferencesUpdater.CHANNEL_NEWSLETTER.equals(channel)) {
                mode = DetailActivity.MODE_NEWSLETTER;
            }

            if (mode == -1) {
                Intent blah = new Intent(context, MainActivity.class);
                blah.putExtras(intent);
                blah.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(blah);
            } else {
                Intent nextIntent = DetailActivity.createIntent(context, mode);
                nextIntent.putExtras(intent);
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                TaskStackBuilder tsb = TaskStackBuilder.create(context);
                tsb.addParentStack(DetailActivity.class);
                tsb.addNextIntent(nextIntent);
                tsb.startActivities();
            }
        } catch (Exception e) {
            android.util.Log.w("PushMessageReceiver", "Failed to open activity.", e);
        }
    }
}
