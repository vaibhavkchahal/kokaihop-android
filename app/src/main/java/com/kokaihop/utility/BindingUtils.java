package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Rajendra Singh on 10/5/17.
 */

public class BindingUtils {

    @BindingAdapter({"app:font"})
    public static void setFont(TextView textView, String fontName) {
        Log.e("Font",fontName);
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(),"fonts/"+fontName));
    }
}
