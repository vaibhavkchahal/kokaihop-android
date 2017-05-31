package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentFollowersFollowingBinding;
import com.kokaihop.home.UserProfileFragment;
import com.kokaihop.userprofile.model.FollowersFollowingList;
import com.kokaihop.userprofile.model.FollowingFollowerUser;
import com.kokaihop.utility.RecyclerViewScrollListener;

import java.util.ArrayList;

public class FollowingFragment extends Fragment implements UserApiCallback {

    private static FollowingFragment fragment;
    private FragmentFollowersFollowingBinding followingBinding;
    private FollowersFollowingAdapter followingAdapter;
    private ArrayList<FollowingFollowerUser> followingUsers;
    private FollowersFollowingViewModel followingViewModel;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followingViewModel = new FollowersFollowingViewModel(this, getContext());
        FollowersFollowingList.getFollowingList().getUsers().clear();
        followingAdapter = new FollowersFollowingAdapter(FollowersFollowingList.getFollowingList().getUsers(), followingViewModel);
        layoutManager = new LinearLayoutManager(this.getContext());

        followingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);
        recyclerView = followingBinding.rvFollowerFollowingList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
        followingViewModel.getFollowingUsers(0);
//        followingBinding.refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                FollowersFollowingList.getFollowingList().getUsers().clear();
//                followingViewModel.getFollowingUsers(0);
//            }
//        });

        return followingBinding.getRoot();
    }

    @Override
    public void showUserProfile() {
        followingUsers = FollowersFollowingList.getFollowingList().getUsers();
//        followingBinding.refreshList.setRefreshing(false);
        followingAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(followingAdapter);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (followingViewModel.getOffset() + followingViewModel.getMax() <= FollowersFollowingList.getFollowingList().getTotal())
                    followingViewModel.getFollowingUsers(followingViewModel.getOffset() + followingViewModel.getMax());
            }

        });
    }

    @Override
    public void followToggeled() {
        ((UserProfileFragment) getParentFragment()).setNotificationCount();
    }
}
