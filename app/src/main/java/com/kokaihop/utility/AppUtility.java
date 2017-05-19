package com.kokaihop.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kokaihop.home.HomeActivity;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class AppUtility {

    public  static  void showHomeScreen(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
