package com.kokaihop.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */

public class SharedPrefUtils {

    private static SharedPreferences prefs;


    public static void setSharedPrefStringData(Context context, String key, String value) {
        if (context != null) {
            prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getSharedPrefStringData(Context context, String key) {
        String value = "";
        if (context != null) {
            prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            value = prefs.getString(key, "");
        }
        return value;
    }

    public static String getSharedPrefStringData(View view, String key) {
        String value = "";
        if (view != null) {
            prefs = view.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            value = prefs.getString(key, "");
        }
        return value;
    }

    public static void setSharedPrefBooleanData(Context context, String key, boolean value) {
        if (context != null) {
            prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static boolean getSharedPrefBooleanData(Context context, String key) {
        boolean value = false;
        if (context != null) {
            prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
            value = prefs.getBoolean(key, false);

        }
        return value;
    }
}
