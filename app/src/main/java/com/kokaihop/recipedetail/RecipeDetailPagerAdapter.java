package com.kokaihop.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailPagerItemBinding;
import com.kokaihop.database.RecipeDetailPagerImages;
import com.kokaihop.utility.CloudinaryUtils;

import io.realm.RealmList;

/**
 * Created by Vaibhav Chahal on 30/5/17.
 */

public class RecipeDetailPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RealmList<RecipeDetailPagerImages> pagerImages;
    private int[] images = new int[]{R.drawable.img_intro, R.drawable.badge_dessert, R.drawable.ic_list_orange_sm};

    public RecipeDetailPagerAdapter(Context context, RealmList<RecipeDetailPagerImages> pagerImages) {
        mContext = context;
        this.pagerImages = pagerImages;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public RecipeDetailPagerAdapter(Context context) {

        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pagerImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecipeDetailPagerItemBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.recipe_detail_pager_item, container, false);
        binding.getRoot().setTag(position);
//        float ratio = (float) 13 / 33;
//        int height = getHeightInAspectRatio(width, ratio);
//        ImageView imageViewRecipe = (ImageView) binding.getRoot().findViewById(R.id.imageview_recipe_pic);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageViewRecipe.getLayoutParams();
//        layoutParams.height = height;
//        layoutParams.width = width;
//        imageViewRecipe.setLayoutParams(layoutParams);
//        LinearLayout.LayoutParams layoutParamsRecipeDay = (LinearLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        binding.setImageUrl(CloudinaryUtils.getImageUrl(String.valueOf(pagerImages.get(position).getPublicId()), "2072", "2072"));
//        binding.setImageUrl(CloudinaryUtils.getImageUrl("23665466", "2072", "2072"));
        container.addView(binding.getRoot());
        return binding.getRoot();
//        View itemView = mLayoutInflater.inflate(R.layout.recipe_detail_pager_item, container, false);
//        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview_recipe_pic);
////        imageView.setImageResource(pagerImages.get(position).getPublicId());
//        Glide.with(mContext).load(CloudinaryUtils.getImageUrl("23665466", "2072", "2072")).into(imageView);
////        imageView.setImageResource(CloudinaryUtils.getImageUrl("23665466", "1072", "1072"));
//        container.addView(itemView);

//        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
