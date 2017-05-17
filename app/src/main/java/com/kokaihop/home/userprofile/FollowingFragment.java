package com.kokaihop.home.userprofile;

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

import java.util.ArrayList;

public class FollowingFragment extends Fragment {

    static FollowingFragment fragment;
    FragmentFollowersFollowingBinding followingBinding;
    FollowersFollowingAdapter followingAdapter;
    ArrayList<User> userList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);

        userList = new ArrayList<>();
        userList.add(new User(true, "AA", "url"));
        userList.add(new User(true, "BB", "url"));
        userList.add(new User(false, "CC", "url"));
        userList.add(new User(true, "DD", "url"));
        userList.add(new User(false, "EE", "url"));
        userList.add(new User(false, "FF", "url"));
        userList.add(new User(true, "GG", "url"));
        userList.add(new User(true, "HH", "url"));
        userList.add(new User(true, "II", "url"));
        userList.add(new User(true, "JJ", "url"));
        userList.add(new User(true, "KK", "url"));
        userList.add(new User(true, "LL", "url"));
        userList.add(new User(true, "MM", "url"));
        setupUsersList();
        return followingBinding.getRoot();
    }

    public void setupUsersList() {
        RecyclerView recyclerView = followingBinding.followersList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followingAdapter = new FollowersFollowingAdapter(userList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
    }


}
