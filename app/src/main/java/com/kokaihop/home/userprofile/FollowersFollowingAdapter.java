package com.kokaihop.home.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowProfileFollowerFollowingBinding;
import com.kokaihop.home.userprofile.model.FollowingFollowerUser;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class FollowersFollowingAdapter extends RecyclerView.Adapter<FollowersFollowingAdapter.ViewHolder> {

    private ArrayList<FollowingFollowerUser> usersList;
    FollowersFollowingViewModel followingViewModel;
    RowProfileFollowerFollowingBinding binding;
    Context context;

    public FollowersFollowingAdapter(ArrayList<FollowingFollowerUser> usersList, FollowersFollowingViewModel followingViewModel) {
        this.usersList = usersList;
        this.followingViewModel = followingViewModel;
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
        final FollowingFollowerUser user = usersList.get(position);
        holder.bind(user);
//        binding.setUser(user);
        Log.e("Sh", SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID));
        Log.e("ID", usersList.get(position).get_id() + " : " + user.getName().getFull());
        if (SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID).equals(user.get_id())) {
            user.setButtonVisibility(View.GONE);
            Log.e("Hidden", "true");
        }

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

        public void bind(FollowingFollowerUser followingFollowerUser) {
            binding.setUser(followingFollowerUser);
            binding.setViewModel(followingViewModel);
            binding.executePendingBindings();
        }
    }
}
