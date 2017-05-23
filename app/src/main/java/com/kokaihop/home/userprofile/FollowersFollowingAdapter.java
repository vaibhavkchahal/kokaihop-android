package com.kokaihop.home.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowProfileFollowerFollowingBinding;
import com.kokaihop.home.userprofile.model.FollowingUser;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class FollowersFollowingAdapter extends RecyclerView.Adapter<FollowersFollowingAdapter.ViewHolder> {

    private ArrayList<FollowingUser> usersList;
    FollowingViewModel followingViewModel;
    RowProfileFollowerFollowingBinding binding;
    Context context;

    public FollowersFollowingAdapter(ArrayList<FollowingUser> usersList) {
        this.usersList = usersList;
        Log.e(usersList.size() + "", "Size");
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_profile_follower_following, parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bind(usersList.get(position));
        Log.e("Sh", SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID));
        Log.e("ID", usersList.get(position).get_id()+ " : " + usersList.get(position).getName().getFull());
        if (SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID).equals(usersList.get(position).get_id())) {
            binding.cbUserFollowing.setVisibility(View.GONE);
            Log.e("Hidden", "true");
        }

        binding.cbUserFollowing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                followingViewModel = new FollowingViewModel(context);
                String userId = usersList.get(position).get_id();
                followingViewModel.toggleFollowing(userId, isChecked);
            }
        });
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
            binding.tvUserName.setOnClickListener(this);
            binding.userPic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        public void bind(FollowingUser followingUser) {
            binding.setUser(followingUser);
            binding.executePendingBindings();
        }
    }
}
