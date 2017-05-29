package com.kokaihop.utility;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vaibhav Chahal on 18/5/17.
 */

public class DateTimeUtils {

    public static String getDate(long date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }


    public static long getOneHoursDiff(long lastUpdateTime) {
        long diffInMillisec = System.currentTimeMillis() - lastUpdateTime;

        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec);
        Logger.e("diffInHours", diffInHours + "");
        return diffInHours;
    }
}
