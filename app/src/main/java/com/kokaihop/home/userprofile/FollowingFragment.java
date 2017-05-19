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

public class FollowingFragment extends Fragment {

    static FollowingFragment fragment;
    FragmentFollowersFollowingBinding followingBinding;
    FollowersFollowingAdapter followingAdapter;
    ArrayList<UserApiResponse> userApiResponseList;

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

        userApiResponseList = new ArrayList<>();
        userApiResponseList.add(new UserApiResponse(true, "AA", "url"));
        userApiResponseList.add(new UserApiResponse(true, "BB", "url"));
        userApiResponseList.add(new UserApiResponse(false, "CC", "url"));
        userApiResponseList.add(new UserApiResponse(true, "DD", "url"));
        userApiResponseList.add(new UserApiResponse(false, "EE", "url"));
        userApiResponseList.add(new UserApiResponse(false, "FF", "url"));
        userApiResponseList.add(new UserApiResponse(true, "gg", "url"));
        userApiResponseList.add(new UserApiResponse(true, "HH", "url"));
        userApiResponseList.add(new UserApiResponse(true, "II", "url"));
        userApiResponseList.add(new UserApiResponse(true, "JJ", "url"));
        userApiResponseList.add(new UserApiResponse(true, "KK", "url"));
        userApiResponseList.add(new UserApiResponse(true, "LL", "url"));
        userApiResponseList.add(new UserApiResponse(true, "MM", "url"));
        setupUsersList();
        return followingBinding.getRoot();
    }

    public void setupUsersList() {
        RecyclerView recyclerView = followingBinding.followersList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followingAdapter = new FollowersFollowingAdapter(userApiResponseList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
    }


}
