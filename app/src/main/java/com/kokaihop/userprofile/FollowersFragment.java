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
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.RecyclerViewScrollListener;

import java.util.ArrayList;

public class FollowersFragment extends Fragment implements UserApiCallback {

    static FollowersFragment fragment;
    FragmentFollowersFollowingBinding followersBinding;
    FollowersFollowingViewModel followersViewModel;
    FollowersFollowingAdapter followersAdapter;
    ArrayList<FollowingFollowerUser> followers;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment getInstance() {
        if (fragment == null) {
            fragment = new FollowersFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);

        followers = new ArrayList<>();
        FollowersFollowingList.getFollowersList().getUsers().clear();
        followersViewModel = new FollowersFollowingViewModel(this, getContext());
        followersAdapter = new FollowersFollowingAdapter(FollowersFollowingList.getFollowersList().getUsers(), followersViewModel);
        layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView = followersBinding.rvFollowerFollowingList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
        followersViewModel.getFollowers(0);
//        followersBinding.refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                FollowersFollowingList.getFollowersList().getUsers().clear();
//                followersViewModel.setDownloading(true);
//                followersViewModel.getFollowers(0);
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (followersViewModel.getOffset() + followersViewModel.getMax() <= followersViewModel.getTotalFollowers())
                    followersViewModel.getFollowers(followersViewModel.getOffset() + followersViewModel.getMax());
            }
        });
        return followersBinding.getRoot();
    }


    //Checking whether the user is following the follower or not
    @Override
    public void showUserProfile() {
//        followers = FollowingFollowersApiResponse.getFollowersApiResponse().getUsers();
        followersAdapter.notifyDataSetChanged();
        ArrayList<String> usersFollowing = User.getInstance().getFollowing();
        for (FollowingFollowerUser user : followers) {
            if (!usersFollowing.contains(user.get_id())) {
                user.setFollowingUser(false);
            }
        }
//        followersBinding.refreshList.setRefreshing(false);
    }

    @Override
    public void followToggeled() {
        ((UserProfileFragment) getParentFragment()).setNotificationCount();
    }
}
