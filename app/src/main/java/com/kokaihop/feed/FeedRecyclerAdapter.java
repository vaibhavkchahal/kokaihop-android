package com.kokaihop.feed;

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
import com.altaworks.kokaihop.ui.databinding.RowRecipeSearchedCountBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.AppUtility.getHeightInAspectRatio;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeListWithAdds = new ArrayList<>();
    public static final int TYPE_ITEM_DAY_RECIPE = 0;
    public static final int TYPE_ITEM_RECIPE = 1;
    public static final int TYPE_ITEM_ADVT = 2;
    public static final int TYPE_ITEM_SEARCH_COUNT = 3;
    private Context context;
    private RelativeLayout.LayoutParams layoutParamsRecipeItem;
    private LinearLayout.LayoutParams layoutParamsRecipeDay;
    private boolean fromSearchedView;
    private int columnsInGrid;

    public FeedRecyclerAdapter(List<Object> list, int numOfComumnInGrid) {
        recipeListWithAdds = list;
        this.columnsInGrid = numOfComumnInGrid;
    }

    public List<Object> getRecipeListWithAdds() {
        return recipeListWithAdds;
    }

    public void setRecipeListWithAdds(List<Object> recipeListWithAdds) {
        this.recipeListWithAdds = recipeListWithAdds;
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
            int width = (point.x - marginInPx) / columnsInGrid;
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
        } else if (viewType == TYPE_ITEM_SEARCH_COUNT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_searched_count, parent, false);
            return new ViewHolderRecipeCount(v);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM_DAY_RECIPE:
                ViewHolderRecipeOfDay holderRecipeOfDay = (ViewHolderRecipeOfDay) holder;
                RecipeRealmObject recipeRealmObjectOfDay = (RecipeRealmObject) recipeListWithAdds.get(position);
                holderRecipeOfDay.binder.setRecipe(recipeRealmObjectOfDay);
                Logger.e("height", layoutParamsRecipeDay.height + ", width " + layoutParamsRecipeDay.width);
                String publicId = "";
                if (recipeRealmObjectOfDay.getCoverImage() != null) {
                    publicId = recipeRealmObjectOfDay.getCoverImage();
                } else if (recipeRealmObjectOfDay.getMainImage() != null) {
                    Logger.e("Recipe Url", recipeRealmObjectOfDay.getMainImage().getPublicId() + "ID");
                    publicId = recipeRealmObjectOfDay.getMainImage().getPublicId();

                }
                holderRecipeOfDay.binder.setRecipeHandler(new RecipeHandler());
                holderRecipeOfDay.binder.setFeedImageUrl(CloudinaryUtils.getImageUrl(publicId, String.valueOf(layoutParamsRecipeDay.width), String.valueOf(layoutParamsRecipeDay.height)));
                holderRecipeOfDay.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE:
                ViewHolderRecipe viewHolderRecipe = (ViewHolderRecipe) holder;
                RecipeRealmObject recipeRealmObject = (RecipeRealmObject) recipeListWithAdds.get(position);
                String publicIdRecipe = "";
                if (recipeRealmObject.getCoverImage() != null) {
                    publicIdRecipe = recipeRealmObject.getCoverImage();
                } else if (recipeRealmObject.getMainImage() != null) {
                    publicIdRecipe = recipeRealmObject.getMainImage().getPublicId();

                }
                viewHolderRecipe.binder.setFeedImageUrl(CloudinaryUtils.getImageUrl(publicIdRecipe, String.valueOf(layoutParamsRecipeItem.width), String.valueOf(layoutParamsRecipeItem.height)));
                int profileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.iv_profile_height_width);
                if (recipeRealmObject.getCreatedBy() != null) {
                    viewHolderRecipe.binder.setProfileImageUrl(CloudinaryUtils.getRoundedImageUrl(recipeRealmObject.getCreatedBy().getProfileImageId(), String.valueOf(profileImageSize), String.valueOf(profileImageSize)));
                }
                if (isFromSearchedView()) {
                    viewHolderRecipe.binder.tvRecipeDate.setVisibility(View.GONE);
                }
                viewHolderRecipe.binder.setRecipe(recipeRealmObject);
                viewHolderRecipe.binder.setPosition(position);
                viewHolderRecipe.binder.setRecipeHandler(new RecipeHandler());
                viewHolderRecipe.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADVT:
                final ViewHolderAdvt viewHolderAdvt = (ViewHolderAdvt) holder;
                final AdView adView = (AdView) recipeListWithAdds.get(position);
                if (viewHolderAdvt.binder.linearLayoutAds.getChildCount() > 0) {
                    viewHolderAdvt.binder.linearLayoutAds.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                // Add the ads.
                viewHolderAdvt.binder.linearLayoutAds.addView(adView);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        int spacingInPixels = adView.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
                        adView.setPadding(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels);
                        adView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        adView.setVisibility(View.GONE);
                    }
                });
                // Load the ad.
                adView.loadAd(new AdRequest.Builder().build());
//                adView.loadAd(new AdRequest.Builder().addTestDevice("B2392C13860FF69BF8F847F0914A2745").build());  //TODO: Remove adTestDevice method for production
                break;
            case TYPE_ITEM_SEARCH_COUNT:
                ViewHolderRecipeCount viewHolderRecipeCount = (ViewHolderRecipeCount) holder;
                SearchRecipeHeader searchRecipeHeader = (SearchRecipeHeader) recipeListWithAdds.get(position);
                String result = String.format(context.getResources().getString(R.string.recipe_results), searchRecipeHeader.getCount());
                viewHolderRecipeCount.binder.textviewRecipeCount.setText(result);
            default:
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = recipeListWithAdds.get(position);
        if (object instanceof RecipeRealmObject) {
            if (isPositionHeader(position) && !isFromSearchedView())
                return TYPE_ITEM_DAY_RECIPE;
            return TYPE_ITEM_RECIPE;
        } else if (object instanceof AdView) {
            return TYPE_ITEM_ADVT;
        } else if (object instanceof SearchRecipeHeader) {
            return TYPE_ITEM_SEARCH_COUNT;
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

    public boolean isFromSearchedView() {
        return fromSearchedView;
    }

    public void setFromSearchedView(boolean fromSearchedView) {
        this.fromSearchedView = fromSearchedView;
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

    public class ViewHolderRecipeCount extends RecyclerView.ViewHolder {
        public RowRecipeSearchedCountBinding binder;


        public ViewHolderRecipeCount(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }
}