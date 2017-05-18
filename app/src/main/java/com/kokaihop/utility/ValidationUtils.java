package com.kokaihop.utility;

import android.content.SharedPreferences;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */

public class ValidationUtils {

    private static SharedPreferences prefs;

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


}
