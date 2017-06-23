package com.kokaihop.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.home.HomeActivity;

import java.io.File;

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

    public static View getImageUrlWithAspectRatio(int height, int width, View view) {
        Point point = getDisplayPoint(view.getContext());
        int aspectWidth = point.x;
        float ratio = (float) height / width;
        int aspectHeight = getHeightInAspectRatio(aspectWidth, ratio);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = aspectHeight;
        layoutParams.width = aspectWidth;
        view.setLayoutParams(layoutParams);
        return view;
    }


    public static void showLoginDialog(final Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.EXTRA_FROM, "loginRequired");
                context.startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static File getTempFile() {
        File photo = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_NAME + "/" + "temp.jpg");
        return photo;

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public static void showKeyboard(View view) {
        Activity activity = (Activity) view.getContext();
        try {
            InputMethodManager input = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
