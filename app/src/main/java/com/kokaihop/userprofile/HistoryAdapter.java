package com.kokaihop.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowHistoryBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.feed.Recipe;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;
    HistoryViewModel historyViewModel;
    RowHistoryBinding binding;
    Context context;
    Point point;

    public HistoryAdapter(ArrayList<Recipe> recipeList, HistoryViewModel historyViewModel) {
        this.recipeList = recipeList;
        this.historyViewModel = historyViewModel;
        Log.e(recipeList.size() + "", "Size");
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_history, parent, false);
        context = parent.getContext();

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
        if (recipe.getMainImagePublicId() != null){
            recipe.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(recipe.getMainImagePublicId(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
        }else{
            Glide.clear(binding.ivRecipeImage);
        }
        binding.ivRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("URL",recipe.getMainImageUrl());
            }
        });

        holder.bind(recipe);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowHistoryBinding binding;

        public ViewHolder(RowHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.tvUserName.setOnClickListener(this);
            binding.ivRecipeImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        public void bind(Recipe recipe) {
            binding.setRecipe(recipe);
            binding.executePendingBindings();
        }
    }
}
