package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kokaihop.feed.maincourse.FeedRecyclerAdapter;
import com.kokaihop.utility.glide.GlideCircularTranform;

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
    public static void bindList(RecyclerView view, List<Object> list) {
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new FeedRecyclerAdapter(list));
    }

    @BindingAdapter({"app:imageUrl", "app:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Glide.with(view.getContext()).load(url).error(error).into(view);
        Logger.i("url cloudnary-->", url);
    }

    @BindingAdapter({"app:imageUrl", "app:error", "app:isCircular"})
    public static void loadImage(ImageView view, String url, Drawable error, boolean isCircular) {
        if (isCircular) {
            Glide.with(view.getContext()).load(url).transform(new GlideCircularTranform(view.getContext())).error(error).into(view);
        } else {
            Glide.with(view.getContext()).load(url).error(error).into(view);
        }
        Logger.i("url cloudnary-->", url);
    }

}
