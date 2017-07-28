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
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

public class FollowingFragment extends Fragment implements UserDataListener {

    private FragmentFollowersFollowingBinding binding;
    private FollowersFollowingAdapter followingAdapter;
    public FollowersFollowingViewModel followingViewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private View noData;
    private User user;

    public FollowingFragment() {
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
        String userId = bundle.getString(Constants.USER_ID);
        if (userId.equals(SharedPrefUtils.getSharedPrefStringData(getActivity(), Constants.USER_ID))) {
            user = User.getInstance();
        } else {
            user = new User();
        }
        followingViewModel = new FollowersFollowingViewModel(this, getContext(), userId, user);
        followingAdapter = new FollowersFollowingAdapter(user.getFollowingList(), followingViewModel, this);
        layoutManager = new CustomLinearLayoutManager(this.getContext());
        this.inflater = inflater;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);
        binding.setViewModel(followingViewModel);
        recyclerView = binding.rvFollowerFollowingList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
        followingViewModel.getFollowingUsers(0);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (followingViewModel.getOffset() + followingViewModel.getMax() <= user.getFollowingCount()) {
                    followingViewModel.getFollowingUsers(followingViewModel.getOffset() + followingViewModel.getMax());
                }
            }

            @Override
            public void getScrolledState(RecyclerView recyclerView) {
            }

        });
        return binding.getRoot();
    }

    @Override
    public void showUserProfile() {
        followingAdapter.notifyDataSetChanged();
//        if (noData == null) {
//            noData = inflater.inflate(R.layout.layout_no_data_available, binding.followerFollowigContainer, false);
//        }
//        if (followingAdapter.getItemCount() <= 0) {
//            if (noData.getParent() != null) {
//                ((ViewGroup) noData.getParent()).removeView(noData);
//            }
//            binding.followerFollowigContainer.addView(noData, 0);
//        } else {
//            binding.followerFollowigContainer.removeView(noData);
//        }
    }

    @Override
    public void followToggeled() {
        if (getParentFragment() instanceof UserProfileFragment) {
            ((UserProfileFragment) getParentFragment()).setNotificationCount();
        } else {
            ((OtherUserProfileFragment) getParentFragment()).setNotificationCount();
        }
    }

    public User getUser() {
        return user;
    }
}
