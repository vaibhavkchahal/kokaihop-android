package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kokaihop.feed.FeedRecyclerAdapter;
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

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
        Logger.i("URL cloudnary-->", url);
    }

    @BindingAdapter({"app:imageUrl", "app:error", "app:placeholder"})
    public static void loadImage(ImageView view, String url, Drawable error, Drawable placeholder) {
        Glide.with(view.getContext()).load(url).placeholder(placeholder).error(error).into(view);
        Logger.i("URL cloudnary-->", url);
    }

    @BindingAdapter({"app:imageUrl", "app:error", "app:placeholder", "app:isCircular",})
    public static void loadImage(ImageView view, String url, Drawable error, Drawable placeholder, boolean isCircular) {
        Logger.i("URL cloudnary-->", url);
        if (isCircular) {
            Glide.with(view.getContext()).load(url).transform(new GlideCircularTranform(view.getContext())).placeholder(placeholder).error(error).into(view);
        } else {
            Glide.with(view.getContext()).load(url).placeholder(placeholder).error(error).into(view);
        }
    }

    /*@BindingAdapter("android:visibility")
    public static void setVisibility(View view, Payload model) {

        view.setVisibility(model.getReplyEvents().isEmpty() ? View.GONE: View.VISIBLE);
    }*/
}
