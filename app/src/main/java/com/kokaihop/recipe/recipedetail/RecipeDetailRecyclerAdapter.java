/*
package com.kokaihop.recipe.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerAdvtItemBinding;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerDayRecipeItemBinding;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerRecipeItemBinding;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.feed.FeedRecyclerAdapter.TYPE_ITEM_DAY_RECIPE;
import static com.kokaihop.feed.FeedRecyclerAdapter.TYPE_ITEM_RECIPE;
import static com.kokaihop.utility.AppUtility.getHeightInAspectRatio;

public class RecipeDetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeListWithAdds = new ArrayList<>();
    public static final int TYPE_ITEM_RECIPE_HEADER = 0;
    public static final int TYPE_ITEM_ADVT = 1;
    public static final int TYPE_ITEM_RECIPE_INGREDIENTS = 2;
    public static final int TYPE_ITEM_DIRECTION = 3;
    public static final int TYPE_ITEM_PROFILE_DETAIL = 4;
    public static final int TYPE_ITEM_PROFILE_COMMENTS = 5;

    private Context context;
    private RelativeLayout.LayoutParams layoutParamsRecipeItem;
    private LinearLayout.LayoutParams layoutParamsRecipeDay;

    public RecipeDetailRecyclerAdapter(List<Object> list) {
        recipeListWithAdds = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        Point point = AppUtility.getDisplayPoint(context);
        int marginInPx = 2 * context.getResources().getDimensionPixelOffset(R.dimen.card_left_right_margin) +
                2 * context.getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        if (viewType == TYPE_ITEM_DAY_RECIPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_day_recipe_item, parent, false);
            //set beam image
            Logger.e("point x", point.x + " point y " + point.y);
            int width = point.x - marginInPx;
            float ratio = (float) 13 / 33;
            int height = getHeightInAspectRatio(width, ratio);
            ImageView imageViewRecipe = (ImageView) v.findViewById(R.id.imageview_recipe_of_day);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageViewRecipe.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            imageViewRecipe.setLayoutParams(layoutParams);
            layoutParamsRecipeDay = (LinearLayout.LayoutParams) imageViewRecipe.getLayoutParams();
            return new ViewHolderRecipeOfDay(v);
        } else if (viewType == TYPE_ITEM_RECIPE) {
            marginInPx = 2 * context.getResources().getDimensionPixelOffset(R.dimen.card_left_right_margin) +
                    4 * context.getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_recipe_item, parent, false);
            Logger.e("point x", point.x + " point y " + point.y);
            int width = (point.x - marginInPx) / 2;
            float ratio = (float) 3 / 4;
            int height = getHeightInAspectRatio(width, ratio);
            ImageView imageViewRecipe = (ImageView) v.findViewById(R.id.iv_recipe);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            imageViewRecipe.setLayoutParams(layoutParams);
            layoutParamsRecipeItem = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
            return new ViewHolderRecipe(v);
        } else if (viewType == TYPE_ITEM_ADVT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_advt_item, parent, false);
            return new ViewHolderAdvt(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM_DAY_RECIPE:
                ViewHolderRecipeOfDay holderRecipeOfDay = (ViewHolderRecipeOfDay) holder;
                Recipe recipeRealmObjectOfDay = (Recipe) recipeListWithAdds.get(position);
                holderRecipeOfDay.binder.setRecipe(recipeRealmObjectOfDay);
                Logger.e("height", layoutParamsRecipeDay.height + ", width " + layoutParamsRecipeDay.width);
                if (recipeRealmObjectOfDay.getMainImagePublicId() != null) {
                    holderRecipeOfDay.binder.setFeedImageUrl(CloudinaryUtils.getImageUrl(recipeRealmObjectOfDay.getMainImagePublicId(), String.valueOf(layoutParamsRecipeDay.width), String.valueOf(layoutParamsRecipeDay.height)));

                }
                holderRecipeOfDay.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE:
                ViewHolderRecipe viewHolderRecipe = (ViewHolderRecipe) holder;
                Recipe recipeRealmObject = (Recipe) recipeListWithAdds.get(position);
                Logger.e("height", layoutParamsRecipeItem.height + ", width " + layoutParamsRecipeItem.width);
                if (recipeRealmObject.getMainImagePublicId() != null) {
                    viewHolderRecipe.binder.setFeedImageUrl(CloudinaryUtils.getImageUrl(recipeRealmObject.getMainImagePublicId(), String.valueOf(layoutParamsRecipeItem.width), String.valueOf(layoutParamsRecipeItem.height)));

                }
                int profileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.iv_profile_height_width);
                if (recipeRealmObject.getCreatedByProfileImageId() != null) {
                    viewHolderRecipe.binder.setProfileImageUrl(CloudinaryUtils.getRoundedImageUrl(recipeRealmObject.getCreatedByProfileImageId(), String.valueOf(profileImageSize), String.valueOf(profileImageSize)));
                }
                viewHolderRecipe.binder.setRecipe(recipeRealmObject);
                viewHolderRecipe.binder.setRecipeHandler(new RecipeHandler());
                viewHolderRecipe.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADVT:
                // TODO add advt.
                break;
            default:
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = recipeListWithAdds.get(position);
        if (object instanceof Recipe) {
            if (isPositionHeader(position))
                return TYPE_ITEM_DAY_RECIPE;
            return TYPE_ITEM_RECIPE;
        } else if (object instanceof AdvtDetail) {
            return TYPE_ITEM_ADVT;
        }
        return -1;


    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return recipeListWithAdds.size();
    }


    public class ViewHolderRecipeOfDay extends RecyclerView.ViewHolder {
        public FeedRecyclerDayRecipeItemBinding binder;

        public ViewHolderRecipeOfDay(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }

    private class ViewHolderRecipe extends RecyclerView.ViewHolder {
        public FeedRecyclerRecipeItemBinding binder;

        ViewHolderRecipe(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    public class ViewHolderAdvt extends RecyclerView.ViewHolder {
        public FeedRecyclerAdvtItemBinding binder;


        public ViewHolderAdvt(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }
}*/
