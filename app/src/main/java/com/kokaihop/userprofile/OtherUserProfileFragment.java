package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

public class OtherUserProfileFragment extends Fragment implements UserDataListener {

    private FragmentOtherUserProfileBinding otherUserProfileBinding;
    OtherUserProfileViewModel otherUserProfileViewModel;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    String userId, friendlyUrl;
    Bundle bundle = new Bundle();

    public OtherUserProfileFragment() {

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

        otherUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_user_profile, container, false);
        otherUserProfileViewModel = new OtherUserProfileViewModel(getContext(), this);
        friendlyUrl = otherUserProfileViewModel.getFriendlyUrlFromDB(userId);
        otherUserProfileViewModel.getUserData(userId);
        setAppBarListener();
        return otherUserProfileBinding.getRoot();
    }

    @Override
    public void showUserProfile() {
        User user = User.getInstance();
        final TabLayout tabLayout = otherUserProfileBinding.tabProfile;
        final int activeColor = Color.parseColor(getString(R.string.user_active_tab_text_color));
        final int inactiveColor = Color.parseColor(getString(R.string.user_inactive_tab_text_color));
        int tabCount = 4;
        int i;
        setCoverImage();
        setProfileImage();
        otherUserProfileBinding.setUser(User.getOtherUser());

        String[] tabTitles = {getActivity().getString(R.string.tab_recipes),
                getActivity().getString(R.string.tab_cookbooks),
                getActivity().getString(R.string.tab_followers),
                getActivity().getString(R.string.tab_following)};

//        TODO: counts should be set here.

        int[] counts = {user.getRecipeCount(),
                user.getFollowers().size(),
                user.getFollowing().size(),
                0};

        viewPager = otherUserProfileBinding.viewpagerProfile;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ProfileAdapter adapter = new ProfileAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        setUpFragmentArguments();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);
        adapter.addFrag(recipeFragment, "Recipes");

        FollowersFragment followersFragment = new FollowersFragment();
        followersFragment.setArguments(bundle);
        adapter.addFrag(followersFragment, "Followers");

        FollowingFragment followingFragment = new FollowingFragment();
        followingFragment.setArguments(bundle);
        adapter.addFrag(followingFragment, "Following");

        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(bundle);
        adapter.addFrag(historyFragment, "History");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(tabCount);
        tabLayout.setupWithViewPager(viewPager);


        for (i = 0; i < (tabCount); i++) {
            TabProfileTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_profile_tab_layout, null, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.text1.setText("" + counts[i]);
            tabBinding.text2.setText(tabTitles[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(activeColor);
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(activeColor);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(inactiveColor);
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(inactiveColor);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(activeColor);
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(activeColor);
                }
            }
        });
        tabLayout.getTabAt(0).select();
    }

    public void setCoverImage() {
        Point point = AppUtility.getDisplayPoint(getContext());
        int width = point.x;
        float ratio = (float) 195 / 320; // to get the image in aspect ratio
        int height = AppUtility.getHeightInAspectRatio(width, ratio);
        ImageView ivCover = otherUserProfileBinding.ivProfileCover;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivCover.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        CloudinaryImage coverImage = User.getInstance().getCoverImage();
        if (coverImage != null) {
            otherUserProfileBinding.setImageCoverUrl(CloudinaryUtils.getImageUrl(coverImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height)));
        }
        otherUserProfileBinding.executePendingBindings();
    }

    //To set the user profile image from cloudinary image-URL
    public void setProfileImage() {
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.user_profile_pic_size);
        int height = width;
        ImageView ivProfile = otherUserProfileBinding.userAvatar;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivProfile.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        CloudinaryImage profileImage = User.getOtherUser().getProfileImage();
        if (profileImage != null) {
            String imageUrl = CloudinaryUtils.getRoundedImageUrl(profileImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height));
            User.getInstance().setProfileImageUrl(imageUrl);
        }
        otherUserProfileBinding.executePendingBindings();
    }

    private void setUpFragmentArguments() {
        bundle.putString(Constants.USER_ID, SharedPrefUtils.getSharedPrefStringData(getActivity(), Constants.USER_ID));
        bundle.putString(Constants.FRIENDLY_URL, friendlyUrl);
    }

    @Override
    public void followToggeled() {

    }

    private void setAppBarListener() {
        AppBarLayout appBarLayout = otherUserProfileBinding.appbar;
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                switch (state) {
                    case COLLAPSED:
                        otherUserProfileBinding.rvToolbarContainer.setVisibility(View.INVISIBLE);
                        break;
                    case EXPANDED:
                        otherUserProfileBinding.rvToolbarContainer.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_DOWN:
                        otherUserProfileBinding.rvToolbarContainer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}
