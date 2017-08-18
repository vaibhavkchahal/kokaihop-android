package com.kokaihop.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailPagerItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kokaihop.database.RecipeDetailPagerImages;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;

import static com.kokaihop.utility.AppUtility.getHeightInAspectRatio;

/**
 * Created by Vaibhav Chahal on 30/5/17.
 */

public class RecipeDetailPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RealmList<RecipeDetailPagerImages> pagerImages = new RealmList<>();
    private RecipeDetailPagerItemBinding binding;
    private Set<String> imageUrlSet = new LinkedHashSet<>();
    private String mainImageWidth, mainImageHeight;

    public RecipeDetailPagerAdapter(Context context, RealmList<RecipeDetailPagerImages> pagerImages, String mainImageWidth, String mainImageHeight) {
        mContext = context;
        this.pagerImages = pagerImages;
        this.mainImageWidth = mainImageWidth;
        this.mainImageHeight = mainImageHeight;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pagerImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.recipe_detail_pager_item, container, false);
        binding.getRoot().setTag(position);
        setImageWithAspectRatio(position, binding);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    private void setImageWithAspectRatio(int position, final RecipeDetailPagerItemBinding binding) {
        String imageUrl;
        Point point = AppUtility.getDisplayPoint(mContext);
        int width = point.x;
        float ratio = (float) 280 / 320;
        int height = getHeightInAspectRatio(width, ratio);
        ImageView imageViewRecipe = (ImageView) binding.getRoot().findViewById(R.id.imageview_recipe_pic);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        imageViewRecipe.setLayoutParams(layoutParams);
        if (position == 0) {
            imageUrl = CloudinaryUtils.getImageUrl(String.valueOf(pagerImages.get(position).getPublicId()), mainImageWidth, mainImageHeight);
            Glide.with(mContext)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .thumbnail(Glide.with(mContext)
                            .load(imageUrl))
                    .override(layoutParams.width, layoutParams.height)
                    .fitCenter()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            binding.imageviewRecipePic.setImageDrawable(resource);
                        }
                    });
        } else {
            imageUrl = CloudinaryUtils.getImageUrl(String.valueOf(pagerImages.get(position).getPublicId()), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height));
            Glide.with(mContext).load(imageUrl).placeholder(R.drawable.ic_recipeplaceholder_lg).error(R.drawable.ic_recipeplaceholder_lg).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageviewRecipePic);
            binding.setImageUrl(imageUrl);
        }
        imageUrlSet.add(imageUrl);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    public String getImageUrl(int position) {
        List<String> imageUrlList = new ArrayList<>(imageUrlSet);
        if (position < imageUrlList.size()) {
            return imageUrlList.get(position);
        } else {
            return "";
        }
    }
}
