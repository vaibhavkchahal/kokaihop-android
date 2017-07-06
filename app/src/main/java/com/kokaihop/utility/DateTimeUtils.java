package com.kokaihop.utility;

import android.content.Context;
import android.text.format.DateUtils;

import com.altaworks.kokaihop.ui.R;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static String getRelativeTimeDisplayDate(Context context, long timeStamp) {
        String relativeTime;
        Date serverDate = new Date(timeStamp);
        long different = new Date().getTime() - serverDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long mDisplayDays, mDisplayHours, mDisplayMinutes;
        mDisplayDays = different / daysInMilli;
        different = different % daysInMilli;
        mDisplayHours = different / hoursInMilli;
        different = different % hoursInMilli;
        mDisplayMinutes = different / minutesInMilli;
        if (mDisplayDays >= 2 * 365) {
            relativeTime = String.format(context.getResources().getString(R.string.years_ago), mDisplayDays / 365);
        } else if (mDisplayDays >= 365 && mDisplayDays < 2 * 365) {
            relativeTime = context.getResources().getString(R.string.year_ago);
        } else if (mDisplayDays >= 60 && mDisplayDays < 365) {
            relativeTime = String.format(context.getResources().getString(R.string.months_ago), mDisplayDays / 30);
        } else if (mDisplayDays >= 30 && mDisplayDays < 60) {
            relativeTime = context.getResources().getString(R.string.month_ago);
        } else if (mDisplayDays >= 14 && mDisplayDays < 30) {
            relativeTime = String.format(context.getResources().getString(R.string.weeks_ago), mDisplayDays / 7);
        } else if (mDisplayDays >= 7 && mDisplayDays < 14) {
            relativeTime = context.getResources().getString(R.string.week_ago);
        } else if (mDisplayDays > 1 && mDisplayDays < 7) {
            relativeTime = (String) DateUtils.getRelativeTimeSpanString(serverDate.getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS);
        } else if (mDisplayDays == 1) {
            relativeTime = context.getResources().getString(R.string.day_ago);
        } else if (mDisplayHours > 1) {
            relativeTime = String.format(context.getResources().getString(R.string.hours_ago), mDisplayHours);
        } else if (mDisplayHours == 1) {
            relativeTime = String.format(context.getResources().getString(R.string.hour_ago), mDisplayHours);
        } else if (mDisplayMinutes >= 1) {
            relativeTime = String.format(context.getResources().getString(R.string.minutes_ago), mDisplayMinutes);
        } else
            relativeTime = context.getResources().getString(R.string.few_seconds_ago);
//        Logger.i(TAG, "relative time: " + relativeTime);
        return relativeTime;
    }
}
