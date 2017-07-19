package com.kokaihop.batch;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.altaworks.kokaihop.ui.R;
import com.batch.android.Batch;
import com.kokaihop.recipedetail.RecipeDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
                String message = intent.getStringExtra("message");
                showNotification(intent);


            }
        } finally {
            PushReceiver.completeWakefulIntent(intent);
        }
    }

    private void showNotification(Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // Build your own notification here...

        // Assuming you have a drawable named notification_icon, can otherwise be anything you want
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(intent.getStringExtra("message"));

        // Create intent
        Intent launchIntent = bindLaunchIntent(intent);
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

        // Call Batch to keep track of that notification
        Batch.Push.onNotificationDisplayed(this, intent);
    }

    private Intent bindLaunchIntent(Intent intent) {
        Intent launchIntent = null;

        try {
            JSONObject customPayLoadJSON = new JSONObject(intent.getStringExtra("customPayload"));
            JSONObject dataJSON = customPayLoadJSON.getJSONObject("data");
            String type = dataJSON.getString("type");
            String badgeType = dataJSON.getString("badgeType");
            String friendlyUrl = dataJSON.getString("friendlyUrl");

            switch (type) {
                case "RECIPE":
                    launchIntent = new Intent(this, RecipeDetailActivity.class);

                    break;
                default:
                    break;
            }
            if (launchIntent != null) {
                launchIntent.putExtra("badgeType", badgeType);
                launchIntent.putExtra("friendlyUrl", friendlyUrl);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return launchIntent;
    }
}
