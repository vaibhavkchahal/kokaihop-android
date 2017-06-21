package com.kokaihop.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowCookbookBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class CookbooksAdapter extends RecyclerView.Adapter<CookbooksAdapter.ViewHolder> {

    private ArrayList<Cookbook> cookbooks;
    RowCookbookBinding binding;
    Context context;
    Point point;
    RecipeHandler recipeHandler;
    Fragment fragment;

    public CookbooksAdapter(Fragment fragment, ArrayList<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
        this.fragment = fragment;
        Log.e(cookbooks.size() + "", "Size");
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_cookbook, parent, false);
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
        final Cookbook cookbook = cookbooks.get(position);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        if (cookbook.getMainImageUrl() != null) {
            cookbook.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(cookbook.getMainImageUrl(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
        } else {
            Glide.clear(binding.ivRecipeImage);
        }
        holder.bind(cookbook);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return cookbooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowCookbookBinding binding;

        public ViewHolder(RowCookbookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(final Cookbook cookbook) {
            binding.setCookbook(cookbook);
            binding.setUser(User.getOtherUser());
            binding.executePendingBindings();
//            binding.historyRow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    recipeHandler.openRecipeDetail(v, cookbook.get_id(), getAdapterPosition());
//                    if (fragment instanceof HistoryFragment) {
//                        displayHistoryChanges();
//                    }
//                }
//            });
        }
    }
}
