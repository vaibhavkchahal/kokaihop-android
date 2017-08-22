package com.kokaihop.batch;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.altaworks.kokaihop.ui.R;
import com.batch.android.Batch;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.recipedetail.RecipeDetailActivity;
import com.kokaihop.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PushService extends IntentService {
    public PushService() {
        super("MyPushService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (Batch.Push.shouldDisplayPush(this, intent)) // Check that the push is valid
            {
                // Custom payload fields. Root keys are always of the string type, due to GCM limitations.
                // Here we'll read the "article_id" key of the following custom payload : {"article_id": 2}
                showNotification(intent);


            }
        } finally {
            PushReceiver.completeWakefulIntent(intent);
        }
    }

    private void showNotification(Intent intent) {
        final Bitmap bitmapLargeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // Build your own notification here...

        Notification notification = new Notification();

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Assuming you have a drawable named notification_icon, can otherwise be anything you want
        builder.setDefaults(notification.defaults)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setLargeIcon(bitmapLargeIcon)
                .setContentText(intent.getStringExtra("msg"));

        if (intent.hasExtra("data")) {

            GoogleAnalyticsHelper.trackEventAction(getString(R.string.pushnotification_category), getString(R.string.pushnotification_received_action));

            Bundle bundle = new Bundle();

            String type = null;
            try {
                JSONObject dataJSON = new JSONObject(intent.getStringExtra("data"));
                type = dataJSON.getString("type");
                String badgeType = dataJSON.getString("badgeType");
                String friendlyUrl = dataJSON.getString("friendlyUrl");
                String message = intent.getStringExtra("msg");
                bundle.putString("message", message);
                bundle.putString("badgeType", badgeType);
                bundle.putString("friendlyUrl", friendlyUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (isAppOnForeground(getApplicationContext())) {
                Intent intentAction = new Intent(Constants.SHOW_DIALOG_ACTION);
                intentAction.putExtras(bundle);
                sendBroadcast(intentAction);


            } else {
                // Create intent
                Intent launchIntent = bindLaunchIntent(type);
                bundle.putString("from", "Notification");
                launchIntent.putExtras(bundle);
                Batch.Push.appendBatchData(intent, launchIntent); // Call this method to add tracking data to your intent to track opens

                // Finish building the notification using the launchIntent
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                // Display your notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


                // "id" is supposed to be a unique id, in order to be able to update the notification if you want.
                // If you don't care about updating it, you can simply make a random it, like below
                int id = (int) (Math.random() * Integer.MAX_VALUE);
                notificationManager.notify(id, builder.build());
            }


        }

        // Call Batch to keep track of that notification
        Batch.Push.onNotificationDisplayed(this, intent);
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    private Intent bindLaunchIntent(String type) {
        Intent launchIntent = null;
        switch (type) {
            case "RECIPE":
                launchIntent = new Intent(this, RecipeDetailActivity.class);

                break;
            default:
                break;
        }
        return launchIntent;
    }
}
