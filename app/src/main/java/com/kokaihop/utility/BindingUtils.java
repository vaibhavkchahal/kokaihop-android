package com.kokaihop.utility;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.kokaihop.utility.glide.GlideCircularTranform;
import com.kokaihop.utility.glide.RoundedCornersTransformation;

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

    /*@BindingAdapter("bind:items")
    public static void bindList(RecyclerView view, List<Object> list) {
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new FeedRecyclerAdapter(list));
    }*/

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

    @BindingAdapter({"app:imageUrl", "app:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Glide.with(view.getContext()).load(url).error(error).into(view);
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

    @BindingAdapter({"app:imageUrl", "app:enableRoundedCorner", "app:radius", "app:margin"})
    public static void loadImage(ImageView view, String url, boolean enableRoundedCorner, int radius, int margin) {
        Logger.i("URL cloudnary-->", url);
        DrawableRequestBuilder<Integer> thumbnail = Glide.with(view.getContext())
                .load(R.drawable.ic_recipeplaceholder_md)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), radius, margin));
        Glide.with(view.getContext()).load(url).bitmapTransform(new RoundedCornersTransformation(view.getContext(), radius, margin)).thumbnail(thumbnail).into(view);
    }
}
