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
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;

public class FollowingFragment extends Fragment implements UserDataListener {

    private FragmentFollowersFollowingBinding followingBinding;
    private FollowersFollowingAdapter followingAdapter;
    public FollowersFollowingViewModel followingViewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

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
        followingViewModel = new FollowersFollowingViewModel(this, getContext(),userId);
        FollowersFollowingList.getFollowingList().getUsers().clear();
        followingAdapter = new FollowersFollowingAdapter(FollowersFollowingList.getFollowingList().getUsers(), followingViewModel);
        layoutManager = new CustomLinearLayoutManager(this.getContext());

        followingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers_following, container, false);
        recyclerView = followingBinding.rvFollowerFollowingList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(followingAdapter);
        followingViewModel.getFollowingUsers(0);

        return followingBinding.getRoot();
    }

    @Override
    public void showUserProfile() {
        followingAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (followingViewModel.getOffset() + followingViewModel.getMax() <= FollowersFollowingList.getFollowingList().getTotal()) {
                    followingViewModel.getFollowingUsers(followingViewModel.getOffset() + followingViewModel.getMax());
                }
                followingAdapter.notifyDataSetChanged();
            }

            @Override
            public void getScrolledState(RecyclerView recyclerView) {
            }

        });
    }

    @Override
    public void followToggeled() {
        if(getParentFragment() instanceof  UserProfileFragment){
            ((UserProfileFragment) getParentFragment()).setNotificationCount();
        }else{
            ((OtherUserProfileFragment) getParentFragment()).setNotificationCount();

        }
    }
}
