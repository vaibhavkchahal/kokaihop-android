package com.kokaihop.home.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentFollowersFollowingBinding;
import com.kokaihop.home.userprofile.model.FollowingApiResponse;
import com.kokaihop.home.userprofile.model.FollowingUser;

import java.util.ArrayList;

public class FollowingFragment extends Fragment implements UserApiCallback{

    private static FollowingFragment fragment;
    private FragmentFollowersFollowingBinding followingBinding;
    private FollowersFollowingAdapter followingAdapter;
    private ArrayList<FollowingUser> followingUsers;
    private FollowingViewModel followingViewModel;

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static FollowingFragment getInstance() {
        if (fragment == null) {
            fragment = new FollowingFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        followingUsers = new ArrayList<>();
        followingViewModel = new FollowingViewModel(this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);
        followingViewModel.getFollowingUsers();
        followingBinding.refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followingViewModel.getFollowingUsers();
            }
        });
        return followingBinding.getRoot();
    }

    public void setupUsersList() {
        followingBinding.refreshList.setRefreshing(false);
        RecyclerView recyclerView = followingBinding.followersList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followingAdapter = new FollowersFollowingAdapter(followingUsers);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
    }


    @Override
    public void showUserProfile() {
        followingUsers = FollowingApiResponse.getInstance().getUsers();
        setupUsersList();
    }
}
