package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.widget.TextView;

/**
 * Created by Vaibhav Chahal on 10/5/17.
 */

public class BindingUtils {

    @BindingAdapter({"app:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(CustomFontFamily.getInstance().getFont(fontName));
    }

    @BindingAdapter({"app:font"})
    public static void setFont(TextInputLayout textInputLayout, String fontName) {
        textInputLayout.setTypeface(CustomFontFamily.getInstance().getFont(fontName));
    }
}
