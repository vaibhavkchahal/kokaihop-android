package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;

import java.util.ArrayList;

public class FollowersFragment extends Fragment implements UserDataListener {

    private FragmentFollowersFollowingBinding followersBinding;
    private FollowersFollowingViewModel followersViewModel;
    private FollowersFollowingAdapter followersAdapter;
    private ArrayList<FollowingFollowerUser> followers;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String userId;
    private View noData;
    private LayoutInflater inflater;

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        userId = bundle.getString(Constants.USER_ID);
        this.inflater = inflater;
        followersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);
        followers = new ArrayList<>();
        FollowersFollowingList.getFollowersList().getUsers().clear();
        followersViewModel = new FollowersFollowingViewModel(this, getContext(), userId);
        followersAdapter = new FollowersFollowingAdapter(FollowersFollowingList.getFollowersList().getUsers(), followersViewModel);
        layoutManager = new CustomLinearLayoutManager(this.getContext());

        followersBinding.setViewModel(followersViewModel);
        recyclerView = followersBinding.rvFollowerFollowingList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followersAdapter);
        followersViewModel.getFollowers(0);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (followersViewModel.getOffset() + followersViewModel.getMax() <= followersViewModel.getTotalFollowers())
                    followersViewModel.getFollowers(followersViewModel.getOffset() + followersViewModel.getMax());
            }

            @Override
            public void getScrolledState(RecyclerView recyclerView) {
            }
        });
        return followersBinding.getRoot();
    }


    //Checking whether the user is following the follower or not
    @Override
    public void showUserProfile() {
        followersAdapter.notifyDataSetChanged();

        if (noData == null) {
            noData = inflater.inflate(R.layout.layout_no_data_available, followersBinding.followerFollowigContainer, false);
        }

        if (followersAdapter.getItemCount() <= 0) {
            if (noData.getParent() == null) {
                followersBinding.followerFollowigContainer.addView(noData, 0);
            }
        } else {
            followersBinding.followerFollowigContainer.removeView(noData);
        }
        ArrayList<String> usersFollowing = User.getInstance().getFollowing();

        followers = FollowersFollowingList.getFollowersList().getUsers();
        for (FollowingFollowerUser user : followers) {
            if (!usersFollowing.contains(user.get_id())) {
                user.setFollowingUser(false);
            }
        }
    }

    @Override
    public void followToggeled() {
        if (getParentFragment() instanceof UserProfileFragment) {
            ((UserProfileFragment) getParentFragment()).setNotificationCount();
        }
    }
}
