package com.kokaihop.analytics;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kokaihop.KokaihopApplication;
import com.kokaihop.utility.Logger;

/**
 * Created by Rajendra Singh on 21/7/17.
 */

public class GoogleAnalyticsHelper {


    private static final String TAG = "google analytics";

    public static void trackScreenName(Activity activity, String screenName) {
// Obtain the shared Tracker instance.
        KokaihopApplication application = (KokaihopApplication) activity.getApplication();
        Tracker tracker = application.getDefaultTracker();

        Logger.i(TAG, "Setting screen name: " + screenName);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    public static void trackEventAction(Activity activity, String category, String action) {

        KokaihopApplication application = (KokaihopApplication) activity.getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(1)
                .build());
    }


    public static void trackEventAction(Activity activity, String category, String action, String label) {
        KokaihopApplication application = (KokaihopApplication) activity.getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(1)
                .build());
    }


    public static void trackEventAction(Activity activity, String category, String action, String label, long value) {
        KokaihopApplication application = (KokaihopApplication) activity.getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    public static void trackEventAction(Activity activity, String category, String action, long value) {
        KokaihopApplication application = (KokaihopApplication) activity.getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(value)
                .build());
    }


}
