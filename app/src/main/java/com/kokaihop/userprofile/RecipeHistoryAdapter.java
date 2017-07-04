package com.kokaihop.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowHistoryRecipeBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.database.RecipeHistoryRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class RecipeHistoryAdapter extends RecyclerView.Adapter<RecipeHistoryAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;
    RowHistoryRecipeBinding binding;
    Context context;
    Point point;
    RecipeHandler recipeHandler;
    Fragment fragment;
    RecipeDataManager recipeDataManager;

    public RecipeHistoryAdapter(Fragment fragment, ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.fragment = fragment;
        Log.e(recipeList.size() + "", "Size");
        recipeHandler = new RecipeHandler();
        recipeDataManager = new RecipeDataManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_history_recipe, parent, false);
        point = AppUtility.getDisplayPoint(context);
        int size = context.getResources().getDimensionPixelSize(R.dimen.history_recipe_image_size);
        ImageView ivCover = binding.ivRecipeImage;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;
        ivCover.setLayoutParams(layoutParams);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        if (recipe.getMainImagePublicId() != null) {
            recipe.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(recipe.getMainImagePublicId(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
        } else {
            Glide.clear(binding.ivRecipeImage);
        }
        holder.bind(recipe);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    //    uppdate the history changes on the user profile screen.
    public void displayHistoryChanges() {
        recipeList.clear();
        ArrayList<RecipeHistoryRealmObject> historyList = new HistoryDataManager().getHistory();
        for (RecipeHistoryRealmObject historyRealmObject : historyList) {
            RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(historyRealmObject.getId());
            recipeList.add(recipeDataManager.getRecipe(recipeRealmObject));
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RowHistoryRecipeBinding binding;

        public ViewHolder(RowHistoryRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(final Recipe recipe) {
            binding.setRecipe(recipe);
            binding.executePendingBindings();
            binding.historyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeHandler.openRecipeDetail(v, recipe.get_id(), getAdapterPosition());
                    if (fragment instanceof HistoryFragment) {
                        displayHistoryChanges();
                    }
                }
            });
        }
    }
}
