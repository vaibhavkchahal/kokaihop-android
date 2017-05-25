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
import com.kokaihop.home.UserProfileFragment;
import com.kokaihop.home.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.home.userprofile.model.FollowingFollowerUser;
import com.kokaihop.utility.EndlessScrollListener;

import java.util.ArrayList;

public class FollowingFragment extends Fragment implements UserApiCallback{

    private static FollowingFragment fragment;
    private FragmentFollowersFollowingBinding followingBinding;
    private FollowersFollowingAdapter followersAdapter;
    private ArrayList<FollowingFollowerUser> followingUsers;
    private FollowersFollowingViewModel followingViewModel;

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
        followingViewModel = new FollowersFollowingViewModel(this,getContext());
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

//    Setting up users list to the recycler view.
    public void setupUsersList() {
        followingBinding.refreshList.setRefreshing(false);
        RecyclerView recyclerView = followingBinding.rvFollowerFollowingList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersAdapter = new FollowersFollowingAdapter(followingUsers,followingViewModel);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {

            }
        });
    }


    @Override
    public void showUserProfile() {
        followingUsers = FollowingFollowersApiResponse.getFollowingInstance().getUsers();
        setupUsersList();
    }

    @Override
    public void followToggeled() {
        ((UserProfileFragment) getParentFragment()).setNotificationCount();
    }
}
