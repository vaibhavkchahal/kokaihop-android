package com.kokaihop.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.altaworks.kokaihop.ui.databinding.RowProfileFollowerFollowingBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.userprofile.model.FollowingFollowerUser;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class FollowersFollowingAdapter extends RecyclerView.Adapter<FollowersFollowingAdapter.ViewHolder> {

    private ArrayList<FollowingFollowerUser> usersList;
    private FollowersFollowingViewModel followingViewModel;
    private RowProfileFollowerFollowingBinding binding;
    private Context context;
    private Point point;

    public FollowersFollowingAdapter(ArrayList<FollowingFollowerUser> usersList, FollowersFollowingViewModel followingViewModel) {
        this.usersList = usersList;
        this.followingViewModel = followingViewModel;
        Log.e(usersList.size() + "", "Size");
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_profile_follower_following, parent, false);
        context = parent.getContext();

        point = AppUtility.getDisplayPoint(context);
        int size = context.getResources().getDimensionPixelOffset(R.dimen.follow_row_image_size);
        ImageView ivCover = binding.userPic;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.userPic.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;
        ivCover.setLayoutParams(layoutParams);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FollowingFollowerUser user = usersList.get(position);
        if(!User.getInstance().getFollowing().contains(user.get_id())){
            user.setFollowingUser(false);
        }
        if (SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID).equals(user.get_id())) {
            user.setButtonVisibility(View.GONE);
        }

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.userPic.getLayoutParams();
        if (user.getProfileImage() != null){
            user.setProfileImageUrl(CloudinaryUtils.getRoundedImageUrl(user.getProfileImage().getCloudinaryId(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
        }else{
            binding.setProfilePic(null);
            Glide.clear(binding.userPic);
        }
        holder.bind(user);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowProfileFollowerFollowingBinding binding;

        public ViewHolder(RowProfileFollowerFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
        }

        public void bind(final FollowingFollowerUser followingFollowerUser) {

            binding.setUser(followingFollowerUser);
            binding.setViewModel(followingViewModel);
            binding.executePendingBindings();
            binding.clUserRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,OtherUserProfileActivity.class);
                    i.putExtra(Constants.USER_ID,followingFollowerUser.get_id());
                    i.putExtra(Constants.FRIENDLY_URL,followingFollowerUser.getFriendlyUrl());
                    ((Activity)context).startActivityForResult(i, Constants.USERPROFILE_REQUEST);
                }
            });
        }
    }
}
