package com.kokaihop.home.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentFollowersFollowingBinding;
import com.kokaihop.home.UserProfileFragment;
import com.kokaihop.home.userprofile.model.FollowingFollowerUser;
import com.kokaihop.home.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.home.userprofile.model.UserApiResponse;

import java.util.ArrayList;

public class FollowersFragment extends Fragment implements UserApiCallback{

    static FollowersFragment fragment;
    FragmentFollowersFollowingBinding followersBinding;
    FollowersFollowingViewModel followersViewModel;
    FollowersFollowingAdapter followersAdapter;
    ArrayList<FollowingFollowerUser> followers;
    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment getInstance() {
        if(fragment==null){
            fragment = new FollowersFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        followers = new ArrayList<>();
        followersViewModel = new FollowersFollowingViewModel(this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_followers_following,container,false);
        followersViewModel.getFollowers();
        followersBinding.refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followersViewModel.getFollowers();
            }
        });
        return followersBinding.getRoot();
    }

    public void setupUsersList(){
        followersBinding.refreshList.setRefreshing(false);
        RecyclerView recyclerView = followersBinding.rvFollowerFollowingList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersAdapter = new FollowersFollowingAdapter(followers,followersViewModel);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
    }

    //Checking whether the user is following the follower or onot
    @Override
    public void showUserProfile() {
        followers = FollowingFollowersApiResponse.getFollowersInstance().getUsers();
        followers = FollowingFollowersApiResponse.getFollowersInstance().getUsers();
        ArrayList<String> usersFollowing = UserApiResponse.getInstance().getFollowing();
        for(FollowingFollowerUser user : followers){
            Log.e(user.get_id() , user.getName().getFull());
            if(! usersFollowing.contains(user.get_id())){
                user.setFollowingUser(false);
            }
        }
        setupUsersList();
    }

    @Override
    public void followToggeled() {
        ((UserProfileFragment) getParentFragment()).setNotificationCount();
    }
}
