package com.kokaihop.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */

public class AppUtility {

    private static SharedPreferences prefs;

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void setSharedPrefStringData(Context context, String key, String value) {
        prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPrefStringData(Context context, String key) {
        prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        String value = prefs.getString(key, null);
        return value;
    }

}
