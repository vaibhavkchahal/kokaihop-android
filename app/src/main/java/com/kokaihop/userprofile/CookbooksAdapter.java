package com.kokaihop.userprofile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowCookbookBinding;
import com.altaworks.kokaihop.ui.databinding.RowMyCookbookBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.cookbooks.CookbookDetailActivity;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class CookbooksAdapter extends RecyclerView.Adapter<CookbooksAdapter.ViewHolder> {

    private ArrayList<Cookbook> cookbooks;
    private RowCookbookBinding cookbookBinding;
    private RowMyCookbookBinding myCookbookBinding;
    private boolean myCookbook;
    private User user;
    private String userFriendlyUrl;

    public CookbooksAdapter(ArrayList<Cookbook> cookbooks, boolean myCookbook, User user, String userFriendlyUrl) {
        this.cookbooks = cookbooks;
        this.myCookbook = myCookbook;
        this.user = user;
        this.userFriendlyUrl = userFriendlyUrl;
        user.setFriendlyUrl(userFriendlyUrl);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        ConstraintLayout.LayoutParams layoutParams;
        ImageView ivCover;
        int size;
        if (myCookbook) {
            myCookbookBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_my_cookbook, parent, false);
            ivCover = myCookbookBinding.ivRecipeBackground;
            layoutParams = (ConstraintLayout.LayoutParams) ivCover.getLayoutParams();
            size = context.getResources().getDimensionPixelSize(R.dimen.history_recipe_image_size);
            layoutParams.height = size;
            layoutParams.width = size;
            ivCover.setLayoutParams(layoutParams);
            return new ViewHolder(myCookbookBinding);

        } else {
            cookbookBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_cookbook, parent, false);
            ivCover = cookbookBinding.ivRecipeImage;
            layoutParams = (ConstraintLayout.LayoutParams) ivCover.getLayoutParams();
            int imageSize = context.getResources().getDimensionPixelSize(R.dimen.history_recipe_image_size);
            layoutParams.height = imageSize;
            layoutParams.width = imageSize;
            ivCover.setLayoutParams(layoutParams);
            return new ViewHolder(cookbookBinding);
        }


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Cookbook cookbook = cookbooks.get(position);

        ConstraintLayout.LayoutParams layoutParams;
        if (myCookbook) {
            float ratio = 3.2f;
            layoutParams = (ConstraintLayout.LayoutParams) myCookbookBinding.ivRecipeBackground.getLayoutParams();
            layoutParams.width = (int) (layoutParams.height * ratio);

            if (cookbook.getMainImageUrl() != null) {
                cookbook.setMainImageUrl(CloudinaryUtils.getImageUrl(cookbook.getMainImageUrl(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
            } else {
                Glide.clear(myCookbookBinding.ivRecipeBackground);
            }
        } else {
            layoutParams = (ConstraintLayout.LayoutParams) cookbookBinding.ivRecipeImage.getLayoutParams();
            if (cookbook.getMainImageUrl() != null) {
                cookbook.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(cookbook.getMainImageUrl(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
            } else {
                Glide.clear(cookbookBinding.ivRecipeImage);
            }
        }
        holder.bind(cookbook);
    }

    @Override
    public int getItemCount() {
        return cookbooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowCookbookBinding cookbookBinding;
        RowMyCookbookBinding myCookbookBinding;

        public ViewHolder(RowCookbookBinding cookbookBinding) {
            super(cookbookBinding.getRoot());
            this.cookbookBinding = cookbookBinding;
        }

        public ViewHolder(RowMyCookbookBinding myCookbookBinding) {
            super(myCookbookBinding.getRoot());
            this.myCookbookBinding = myCookbookBinding;
        }


        public void bind(final Cookbook cookbook) {
            if (myCookbook) {
                myCookbookBinding.setCookbook(cookbook);
                myCookbookBinding.setUser(user);
                myCookbookBinding.executePendingBindings();
                myCookbookBinding.clMyCookbookRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCookbookDetail(v, userFriendlyUrl, cookbook.getFriendlyUrl(), cookbook.getName());
                    }
                });
            } else {
                cookbookBinding.setCookbook(cookbook);
                cookbookBinding.setUser(user);
                cookbookBinding.executePendingBindings();
                cookbookBinding.clCookbookRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCookbookDetail(v, userFriendlyUrl, cookbook.getFriendlyUrl(), cookbook.getName());
                    }
                });
            }
        }

        private void openCookbookDetail(View v, String userFriendlyUrl, String cookbookFriendlyUrl, String cookbookTitle) {
            Intent i = new Intent(v.getContext(), CookbookDetailActivity.class);
            i.putExtra(Constants.USER_FRIENDLY_URL, userFriendlyUrl);
            i.putExtra(Constants.COOKBOOK_FRIENDLY_URL, cookbookFriendlyUrl);
            i.putExtra(Constants.COOKBOOK_TITLE, cookbookTitle);
            v.getContext().startActivity(i);
        }
    }
}
