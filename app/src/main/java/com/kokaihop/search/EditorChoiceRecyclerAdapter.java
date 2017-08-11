package com.kokaihop.search;

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
import com.altaworks.kokaihop.ui.databinding.EditorChoiceRecipeItemBinding;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.AppUtility.getHeightInAspectRatio;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class EditorChoiceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeListWithAdds = new ArrayList<>();
    private Context context;
    private RelativeLayout.LayoutParams layoutParamsRecipeItem;
    private LinearLayout.LayoutParams layoutParamsRecipeDay;
    private boolean fromSearchedView;
    private int columnsInGrid;

    public EditorChoiceRecyclerAdapter(List<Object> list, int numOfComumnInGrid) {
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
                4 * context.getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_choice_recipe_item, parent, false);
        Logger.e("point x", point.x + " point y " + point.y);
        double widthRelative = (point.x - marginInPx) / (columnsInGrid + 0.8);
        int width = (int) widthRelative;
        float ratio = (float) 3 / 4;
        int height = getHeightInAspectRatio(width, ratio);
        ImageView imageViewRecipe = (ImageView) v.findViewById(R.id.iv_recipe);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        imageViewRecipe.setLayoutParams(layoutParams);
        layoutParamsRecipeItem = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        return new ViewHolderRecipe(v);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderRecipe viewHolderRecipe = (ViewHolderRecipe) holder;
        RecipeRealmObject recipeRealmObject = (RecipeRealmObject) recipeListWithAdds.get(position);
        String publicIdRecipe = "";
        if (recipeRealmObject.getCoverImage() != null) {
            publicIdRecipe = recipeRealmObject.getCoverImage();
        } else if (recipeRealmObject.getMainImage() != null) {
            publicIdRecipe = recipeRealmObject.getMainImage().getPublicId();

        }
        viewHolderRecipe.binder.setFeedImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(publicIdRecipe, String.valueOf(layoutParamsRecipeItem.width), String.valueOf(layoutParamsRecipeItem.height)));
        viewHolderRecipe.binder.setRecipe(recipeRealmObject);
        viewHolderRecipe.binder.setPosition(position);
        viewHolderRecipe.binder.setRecipeHandler(new RecipeHandler());
        viewHolderRecipe.binder.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return recipeListWithAdds.size();
    }

    public boolean isFromSearchedView() {
        return fromSearchedView;
    }

    private class ViewHolderRecipe extends RecyclerView.ViewHolder {
        public EditorChoiceRecipeItemBinding binder;

        ViewHolderRecipe(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }
}