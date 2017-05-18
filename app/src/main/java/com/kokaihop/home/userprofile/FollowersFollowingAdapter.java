package com.kokaihop.home.userprofile;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowProfileFollowerFollowingBinding;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class FollowersFollowingAdapter extends RecyclerView.Adapter<FollowersFollowingAdapter.ViewHolder>{

    private ArrayList<User> usersList;

    public FollowersFollowingAdapter(ArrayList<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowProfileFollowerFollowingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_profile_follower_following,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        return usersList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RowProfileFollowerFollowingBinding binding;

        public ViewHolder(RowProfileFollowerFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.userName.setOnClickListener(this);
            binding.userPic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("User",usersList.get(getAdapterPosition()).getName());
        }

        public void bind(User user){
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }
}