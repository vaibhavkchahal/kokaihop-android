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
import com.kokaihop.home.userprofile.model.UserApiResponse;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    static FollowersFragment fragment;
    FragmentFollowersFollowingBinding followersBinding;
    FollowersFollowingAdapter followersAdapter;
    ArrayList<UserApiResponse> userApiResponseList;
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
        userApiResponseList = new ArrayList<>();
        userApiResponseList.add(new UserApiResponse(true,"A","url"));
        userApiResponseList.add(new UserApiResponse(true,"B","url"));
        userApiResponseList.add(new UserApiResponse(false,"C","url"));
        userApiResponseList.add(new UserApiResponse(true,"D","url"));
        userApiResponseList.add(new UserApiResponse(false,"E","url"));
        userApiResponseList.add(new UserApiResponse(false,"F","url"));
        userApiResponseList.add(new UserApiResponse(true,"g","url"));
        userApiResponseList.add(new UserApiResponse(true,"H","url"));
        userApiResponseList.add(new UserApiResponse(true,"I","url"));
        setupUsersList();
        return followersBinding.getRoot();
    }

    public void setupUsersList(){
        RecyclerView recyclerView = followersBinding.followersList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersAdapter = new FollowersFollowingAdapter(userApiResponseList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
    }
}
