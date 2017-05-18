package com.kokaihop.feed;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerAdvtItemBinding;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerDayRecipeItemBinding;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerRecipeItemBinding;
import com.kokaihop.database.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    public static final int TYPE_ITEM_DAY_RECIPE = 0;
    public static final int TYPE_ITEM_RECIPE = 1;
    public static final int TYPE_ITEM_ADVT = 2;

    public FeedRecyclerAdapter(List<Recipe> list) {
        recipeList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_DAY_RECIPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_day_recipe_item, parent, false);
            return new ViewHolderRecipeOfDay(v);
        } else if (viewType == TYPE_ITEM_RECIPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_recipe_item, parent, false);
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
                Recipe recipeOfDay = recipeList.get(position);
                holderRecipeOfDay.binder.setRecipe(recipeOfDay);
                holderRecipeOfDay.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE:
                ViewHolderRecipe viewHolderRecipe = (ViewHolderRecipe) holder;
                Recipe recipe = recipeList.get(position);
                viewHolderRecipe.binder.setRecipe(recipe);
                viewHolderRecipe.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADVT:
//                ViewHolderAdvt itemRowHolder = (ViewHolderRecipe) holder;
//                Recipe recipe = recipeList.get(position);
//                itemRowHolder.binder.setRecipe(recipe);
//                itemRowHolder.binder.executePendingBindings();
                break;

            default:
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_ITEM_DAY_RECIPE;
        return TYPE_ITEM_RECIPE;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
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
}