package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.kokaihop.database.Recipe;
import com.kokaihop.feed.FeedRecyclerAdapter;

import java.util.List;

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

    @BindingAdapter("bind:items")
    public  static void bindList(RecyclerView view, List<Recipe> list) {
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new FeedRecyclerAdapter(list));
    }
}
