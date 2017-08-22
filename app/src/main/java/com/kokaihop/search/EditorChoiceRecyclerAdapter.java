package com.kokaihop.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private int columnsInGrid;

    public EditorChoiceRecyclerAdapter(List<Object> list, int numOfComumnInGrid) {
        recipeListWithAdds = list;
        this.columnsInGrid = numOfComumnInGrid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        Point point = AppUtility.getDisplayPoint(context);
        int marginInPx = columnsInGrid * 2 * context.getResources().getDimensionPixelOffset(R.dimen.editor_choice_item_padding) +
                columnsInGrid * context.getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_choice_recipe_item, parent, false);
        Logger.e("point x", point.x + " point y " + point.y);
        double widthRelative;
        if (point.x > point.y) {
            widthRelative = (point.y - marginInPx) / (columnsInGrid + 0.8);
        } else {
            widthRelative = (point.x - marginInPx) / (columnsInGrid + 0.8);
        }
        int width = (int) widthRelative;
        float ratio = (float) 3 / 4;
        int height = getHeightInAspectRatio(width, ratio);
        ImageView imageViewRecipe = (ImageView) v.findViewById(R.id.iv_recipe);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        imageViewRecipe.setLayoutParams(layoutParams);
        layoutParamsRecipeItem = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
        RelativeLayout relativeLayoutEditorChoice = (RelativeLayout) v.findViewById(R.id.rl_editor_choice_item);
        GridLayoutManager.LayoutParams layoutParams1 = (GridLayoutManager.LayoutParams) relativeLayoutEditorChoice.getLayoutParams();
        layoutParams1.width = width + 3 * relativeLayoutEditorChoice.getPaddingBottom();
        relativeLayoutEditorChoice.setLayoutParams(layoutParams1);
        return new ViewHolderRecipe(v);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderRecipe viewHolderRecipe = (ViewHolderRecipe) holder;
        RecipeRealmObject recipeRealmObject = (RecipeRealmObject) recipeListWithAdds.get(position);
        String publicIdRecipe = "";
        if (position == 0) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            int marginInPx = holder.itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.editor_choice_layout_margin_start);
//            layoutParams.setMarginStart(holder.itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.editor_choice_item_padding));
            layoutParams.setMarginStart(marginInPx);
            holder.itemView.setLayoutParams(layoutParams);
        }
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

    private class ViewHolderRecipe extends RecyclerView.ViewHolder {
        public EditorChoiceRecipeItemBinding binder;

        ViewHolderRecipe(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }
}