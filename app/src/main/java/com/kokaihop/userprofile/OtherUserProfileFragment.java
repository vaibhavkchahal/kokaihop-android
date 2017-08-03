package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentOtherUserProfileBinding;
import com.altaworks.kokaihop.ui.databinding.TabProfileTabLayoutBinding;
import com.kokaihop.customviews.AppBarStateChangeListener;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.NotificationCount;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

public class OtherUserProfileFragment extends Fragment implements UserDataListener {

    private FragmentOtherUserProfileBinding binding;
    private OtherUserProfileViewModel otherUserProfileViewModel;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private String userId, friendlyUrl;
    private Bundle bundle = new Bundle();

    private TabLayout tabLayout;
    private User user;

    private int selectedTabPosition = 0;
    ArrayList<NotificationCount> notificationCount;

    public OtherUserProfileFragment() {
        user = new User();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        userId = this.getArguments().getString(Constants.USER_ID);
        friendlyUrl = this.getArguments().getString(Constants.FRIENDLY_URL);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_user_profile, container, false);
        otherUserProfileViewModel = new OtherUserProfileViewModel(getContext(), this, user);
        otherUserProfileViewModel.getUserData(userId, friendlyUrl);
        setAppBarListener();
        notificationCount = new ArrayList<>();
        binding.setViewModel(otherUserProfileViewModel);
        tabLayout = binding.tabProfile;
        binding.srlProfileRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                binding.srlProfileRefresh.setEnabled(verticalOffset == 0);
            }
        });

        if ((SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.USER_ID)).equals(userId)) {
            binding.btnFollow.setVisibility(View.INVISIBLE);
        }
        return binding.getRoot();
    }

    public void refreshData() {
        selectedTabPosition = tabLayout.getSelectedTabPosition();
        otherUserProfileViewModel.getUserData(userId, friendlyUrl);
        binding.srlProfileRefresh.setRefreshing(false);
    }

    @Override
    public void showUserProfile() {
        if (this.isVisible()) {
            final TabLayout tabLayout = binding.tabProfile;
            final int activeColor = Color.parseColor(getString(R.string.user_active_tab_text_color));
            final int inactiveColor = Color.parseColor(getString(R.string.user_inactive_tab_text_color));
            int tabCount = 4;
            int i;
            setCoverImage();
            setProfileImage();
            binding.setUser(user);

            String[] tabTitles = {getActivity().getString(R.string.tab_recipes),
                    getActivity().getString(R.string.tab_cookbooks),
                    getActivity().getString(R.string.tab_followers),
                    getActivity().getString(R.string.tab_following)};

            viewPager = binding.viewpagerProfile;
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            notificationCount.add(new NotificationCount());
            notificationCount.add(new NotificationCount());
            notificationCount.add(new NotificationCount());
            notificationCount.add(new NotificationCount());
            setNotificationCount();
            ProfileAdapter adapter = new ProfileAdapter(getChildFragmentManager(), tabLayout.getTabCount());
            setUpFragmentArguments();
            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);
            adapter.addFrag(recipeFragment, getActivity().getString(R.string.tab_recipes));
            CookbooksFragment cookbooksFragment = new CookbooksFragment();
            cookbooksFragment.setArguments(bundle);
            adapter.addFrag(cookbooksFragment, getActivity().getString(R.string.tab_cookbooks));
            FollowersFragment followersFragment = new FollowersFragment();
            followersFragment.setArguments(bundle);
            adapter.addFrag(followersFragment, getActivity().getString(R.string.tab_followers));
            FollowingFragment followingFragment = new FollowingFragment();
            followingFragment.setArguments(bundle);
            adapter.addFrag(followingFragment, getActivity().getString(R.string.tab_following));
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(tabCount);
            tabLayout.setupWithViewPager(viewPager);
            for (i = 0; i < (tabCount); i++) {
                TabProfileTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_profile_tab_layout, null, false);
                View tabView = tabBinding.getRoot();
                tabLayout.getTabAt(i).setCustomView(tabView);
                tabBinding.setNotification(notificationCount.get(i));
                tabBinding.text2.setText(tabTitles[i]);
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getCustomView() != null) {
                        ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(activeColor);
                        if (tab.getCustomView().findViewById(R.id.text2) != null) {
                            ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(activeColor);
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if (tab.getCustomView() != null) {
                        ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(inactiveColor);
                        if (tab.getCustomView().findViewById(R.id.text2) != null) {
                            ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(inactiveColor);
                        }
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if (tab.getCustomView() != null) {
                        ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(activeColor);
                        if (tab.getCustomView().findViewById(R.id.text2) != null) {
                            ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(activeColor);
                        }
                    }
                }
            });
            if (tabLayout.getTabAt(selectedTabPosition) != null)
                tabLayout.getTabAt(selectedTabPosition).select();
        }
    }

    public void setCoverImage() {
        Point point = AppUtility.getDisplayPoint(getContext());
        int width = point.x;
        float ratio = (float) 195 / 320; // to get the image in aspect ratio
        int height = AppUtility.getHeightInAspectRatio(width, ratio);
        ImageView ivCover = binding.ivProfileCover;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivCover.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        CloudinaryImage coverImage = User.getInstance().getCoverImage();
        if (coverImage != null) {
            binding.setImageCoverUrl(CloudinaryUtils.getImageUrl(coverImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height)));
        }
        binding.executePendingBindings();
    }

    //To set the user profile image from cloudinary image-URL
    public void setProfileImage() {
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.user_profile_pic_size);
        int height = width;
        ImageView ivProfile = binding.userAvatar;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivProfile.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        CloudinaryImage profileImage = User.getOtherUser().getProfileImage();
        if (profileImage != null) {
            String imageUrl = CloudinaryUtils.getRoundedImageUrl(profileImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height));
            User.getOtherUser().setProfileImageUrl(imageUrl);
        }
        binding.executePendingBindings();
    }

    private void setUpFragmentArguments() {
        bundle.putString(Constants.USER_ID, userId);
        bundle.putString(Constants.FRIENDLY_URL, friendlyUrl);
    }

    @Override
    public void followToggeled() {
        setNotificationCount();
        refreshFollowersList();
    }

    private void setAppBarListener() {
        AppBarLayout appBarLayout = binding.appbar;
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                switch (state) {
                    case COLLAPSED:
                        binding.rvToolbarContainer.setVisibility(View.INVISIBLE);
                        break;
                    case EXPANDED:
                        binding.rvToolbarContainer.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_DOWN:
                        binding.rvToolbarContainer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    public void setNotificationCount() {
        notificationCount.get(Constants.TAB_OTHER_RECIPES).setCount(user.getRecipeCount());
        notificationCount.get(Constants.TAB_OTHER_COOKBOOKS).setCount(user.getRecipesCollectionCount());
        notificationCount.get(Constants.TAB_OTHER_FOLLOWERS).setCount(user.getFollowers() == null ? 0 : user.getFollowers().size());
        notificationCount.get(Constants.TAB_OTHER_FOLLOWINGS).setCount(user.getFollowers() == null ? 0 : user.getFollowing().size());
    }

    public void refreshFollowersList(){
        ProfileAdapter profileAdapter = (ProfileAdapter) viewPager.getAdapter();
        Fragment fragment = profileAdapter.getItem(2);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

}
