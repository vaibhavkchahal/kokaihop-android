package com.kokaihop.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kokaihop.utility.Logger;

import static com.kokaihop.KokaihopApplication.getDefaultTracker;
/**
 * Created by Rajendra Singh on 21/7/17.
 */

public class GoogleAnalyticsHelper {


    private static final String TAG = "google analytics";

    public static void trackScreenName(String screenName) {
// Obtain the shared Tracker instance.
        Tracker tracker = getDefaultTracker();

        Logger.i(TAG, "Setting screen name: " + screenName);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    public static void trackEventAction(String category, String action) {
        Tracker tracker = getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(1)
                .build());
    }


    public static void trackEventAction( String category, String action, String label) {
        Tracker tracker = getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(1)
                .build());
    }


    public static void trackEventAction(String category, String action, String label, long value) {
        Tracker tracker = getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    public static void trackEventAction( String category, String action, long value) {
        Tracker tracker = getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(value)
                .build());
    }


}
