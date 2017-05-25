package com.kokaihop.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;

import com.kokaihop.home.HomeActivity;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class AppUtility {

    public static void showHomeScreen(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    public static Point getDisplayPoint(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getHeightInAspectRatio(int width, float ratio) {
        float height = (width * ratio);
        return (int) height;
    }

    public static int getHeightIn4Into3Ratio(int width) {
        int height = (width * 4) / 3;
        return height;
    }

}
