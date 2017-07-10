package com.kokaihop.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowAddToCookbookBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class AddToCookbookAdapter extends RecyclerView.Adapter<AddToCookbookAdapter.ViewHolder> {

    private ArrayList<Cookbook> cookbooks;
    private RowAddToCookbookBinding binding;
    private User user;
    Context context;
    private JSONObject collectionMapping;
    private String recipeId, accessToken;
    AddToCookbookViewModel viewModel;

    public AddToCookbookAdapter(Context context, AddToCookbookViewModel viewModel, ArrayList<Cookbook> cookbooks, String friendlyUrl, String collectionMapping, String recipeId) {
        this.context = context;
        this.cookbooks = cookbooks;
        user = User.getInstance();
        this.recipeId = recipeId;
        this.viewModel = viewModel;
        this.accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        user.setFriendlyUrl(friendlyUrl);
        try {
            this.collectionMapping = new JSONObject(collectionMapping);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        ConstraintLayout.LayoutParams layoutParams;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_add_to_cookbook, parent, false);
        ImageView ivCover = binding.ivRecipeImage;
        layoutParams = (ConstraintLayout.LayoutParams) ivCover.getLayoutParams();
        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.history_recipe_image_size);
        layoutParams.height = imageSize;
        layoutParams.width = imageSize;
        ivCover.setLayoutParams(layoutParams);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Cookbook cookbook = cookbooks.get(position);

        ConstraintLayout.LayoutParams layoutParams;
        layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        if (cookbook.getMainImageUrl() != null) {
            cookbook.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(cookbook.getMainImageUrl(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
        } else {
            Glide.clear(binding.ivRecipeImage);
        }
        holder.bind(cookbook);
    }

    @Override
    public int getItemCount() {
        return cookbooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowAddToCookbookBinding binding;

        public ViewHolder(RowAddToCookbookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Cookbook cookbook) {
            binding.setCookbook(cookbook);
            binding.setUser(user);
            if (cookbook.getFriendlyUrl().equals(Constants.FAVORITE_RECIPE_FRIENDLY_URL)) {
                binding.clCookbookRow.setMaxHeight(0);
            }
            try {
                cookbook.setContains(collectionMapping.getJSONArray(cookbook.get_id()).toString().contains(recipeId));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            binding.executePendingBindings();
            binding.clCookbookRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean added = binding.cbRecipeAdded.isChecked();
                    binding.cbRecipeAdded.setChecked(!added);
                    if (!added) {
                        viewModel.addToCookbook(cookbook,recipeId);
                    } else {
                        viewModel.removeFromCookbook(cookbook,recipeId, getAdapterPosition());
                    }
                }
            });
        }
    }
}
