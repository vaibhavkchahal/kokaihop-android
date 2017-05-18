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

public class FollowersFragment extends Fragment {

    static FollowersFragment fragment;
    FragmentFollowersFollowingBinding followersBinding;
    FollowersFollowingAdapter followersAdapter;
    ArrayList<User> userList;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        followersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_followers_following,container,false);
        userList = new ArrayList<>();
        userList.add(new User(true,"A","url"));
        userList.add(new User(true,"B","url"));
        userList.add(new User(false,"C","url"));
        userList.add(new User(true,"D","url"));
        userList.add(new User(false,"E","url"));
        userList.add(new User(false,"F","url"));
        userList.add(new User(true,"g","url"));
        userList.add(new User(true,"H","url"));
        userList.add(new User(true,"I","url"));
        setupUsersList();
        return followersBinding.getRoot();
    }

    public void setupUsersList(){
        RecyclerView recyclerView = followersBinding.followersList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersAdapter = new FollowersFollowingAdapter(userList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
    }


}
