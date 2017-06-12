package com.kokaihop.home;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentUserProfileBinding;
import com.altaworks.kokaihop.ui.databinding.FragmentUserProfileSignUpBinding;
import com.altaworks.kokaihop.ui.databinding.TabProfileTabLayoutBinding;
import com.altaworks.kokaihop.ui.databinding.TabProfileTabLayoutStvBinding;
import com.kokaihop.editprofile.EditProfileViewModel;
import com.kokaihop.editprofile.SettingsActivity;
import com.kokaihop.userprofile.FollowersFragment;
import com.kokaihop.userprofile.FollowingFragment;
import com.kokaihop.userprofile.HistoryFragment;
import com.kokaihop.userprofile.ProfileAdapter;
import com.kokaihop.userprofile.RecipeFragment;
import com.kokaihop.userprofile.UserDataListener;
import com.kokaihop.userprofile.UserProfileViewModel;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.NotificationCount;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;
import static com.kokaihop.utility.Constants.ACCESS_TOKEN;

public class UserProfileFragment extends Fragment implements UserDataListener {
    private static UserProfileFragment fragment;
    private FragmentUserProfileBinding userProfileBinding;
    FragmentUserProfileSignUpBinding userProfileSignUpBinding;
    UserProfileViewModel userViewModel;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private ViewGroup container;
    private Point point;
    TabLayout tabLayout;
    int selectedTabPosition = 0;

    ArrayList<NotificationCount> notificationCount;

    public UserProfileFragment() {

    }

    public static UserProfileFragment getInstance() {

        if (fragment == null) {
            fragment = new UserProfileFragment();
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
        this.inflater = inflater;
        this.container = container;
        String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), ACCESS_TOKEN);

        if (accessToken != null && !accessToken.isEmpty()) {
//            showUserProfile();
            userProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
            userViewModel = new UserProfileViewModel(getContext(), this);
            userViewModel.getUserData();
            userViewModel.fetchUserDataFromDB();
            userProfileBinding.setViewModel(userViewModel);

            userProfileBinding.srlProfileRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    selectedTabPosition = tabLayout.getSelectedTabPosition();
                    userViewModel.getUserData();
                    userProfileBinding.srlProfileRefresh.setRefreshing(false);
                }
            });

            userProfileBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    userProfileBinding.srlProfileRefresh.setEnabled(verticalOffset == 0);
                }
            });

            userProfileBinding.userAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.e("User profile","Image Clicked");
                    selectImage();
                }
            });

            return userProfileBinding.getRoot();
        } else {
            showSignUpScreen();
            return userProfileSignUpBinding.getRoot();
        }
    }

    public void showSignUpScreen() {
        userProfileSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile_sign_up, container, false);
        userProfileSignUpBinding.signUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogSignUp signUp = (CustomDialogSignUp) DialogFragment.instantiate(getActivity(), "com.kokaihop.home.CustomDialogSignUp");
                signUp.show(getFragmentManager(), "");
//                startActivity(new Intent(getContext(), SignUpActivity.class));
//                TODO:to be Checked
            }
        });
    }

    @Override
    public void showUserProfile() {
        User user = User.getInstance();
        final int activeColor = Color.parseColor(getString(R.string.user_active_tab_text_color));
        final int inactiveColor = Color.parseColor(getString(R.string.user_inactive_tab_text_color));
        tabLayout = userProfileBinding.tabProfile;
        notificationCount = new ArrayList<>();
        int tabCount = 4;
        int i;
        setCoverImage();
        setProfileImage();
        userProfileBinding.setUser(User.getInstance());

        String[] tabTitles = {getActivity().getString(R.string.tab_recipes),
                getActivity().getString(R.string.tab_followers),
                getActivity().getString(R.string.tab_following),
                getActivity().getString(R.string.tab_history)};
//        TODO: counts should be set here.
        notificationCount.add(new NotificationCount());
        notificationCount.add(new NotificationCount());
        notificationCount.add(new NotificationCount());
        setNotificationCount();

        viewPager = userProfileBinding.viewpagerProfile;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

//        ProfileAdapter adapter = new ProfileAdapter(getFragmentManager(), tabLayout.getTabCount());
        ProfileAdapter adapter = new ProfileAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        adapter.addFrag(new RecipeFragment(), "Recipes");
        adapter.addFrag(new FollowersFragment(), "Followers");
        adapter.addFrag(new FollowingFragment(), "Following");
        adapter.addFrag(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(tabCount);
        tabLayout.setupWithViewPager(viewPager);


        for (i = 0; i < (tabCount - 1); i++) {
            TabProfileTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_profile_tab_layout, null, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.setNotification(notificationCount.get(i));
            tabBinding.text2.setText(tabTitles[i]);

        }
        TabProfileTabLayoutStvBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_profile_tab_layout_stv, null, false);
        View tabView = tabBinding.getRoot();
        tabLayout.getTabAt(i).setCustomView(tabView);
        tabBinding.text1.setText(tabTitles[i]);
        tabBinding.text1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history, 0, 0);

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
        userProfileBinding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("Setting","Clicked");
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });

        tabLayout.getTabAt(selectedTabPosition).select();
    }

    @Override
    public void followToggeled() {

    }

    public void setNotificationCount() {
        User user = User.getInstance();
        notificationCount.get(Constants.TAB_RECIPES).setCount(user.getRecipeCount());
        notificationCount.get(Constants.TAB_FOLLOWERS).setCount(user.getFollowers() == null ? 0 : user.getFollowers().size());
        notificationCount.get(Constants.TAB_FOLLOWINGS).setCount(user.getFollowers() == null ? 0 : user.getFollowing().size());
    }

    public void setCoverImage() {
        point = AppUtility.getDisplayPoint(getContext());
        int width = point.x;
        float ratio = (float) 195 / 320; // to get the image in aspect ratio
        int height = AppUtility.getHeightInAspectRatio(width, ratio);
        ImageView ivCover = userProfileBinding.ivProfileCover;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivCover.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        CloudinaryImage coverImage = User.getInstance().getCoverImage();
        if (coverImage != null) {
            userProfileBinding.setImageCoverUrl(CloudinaryUtils.getImageUrl(coverImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height)));
        }
        userProfileBinding.executePendingBindings();
    }


    //To set the user profile image from cloudinary image-URL
    public void setProfileImage() {

        int width = getContext().getResources().getDimensionPixelSize(R.dimen.user_profile_pic_size);
        int height = width;
        ImageView ivProfile = userProfileBinding.userAvatar;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivProfile.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        CloudinaryImage profileImage = User.getInstance().getProfileImage();
        if (profileImage != null) {
            String imageUrl = CloudinaryUtils.getRoundedImageUrl(profileImage.getCloudinaryId(), String.valueOf(coverLayoutParams.width), String.valueOf(coverLayoutParams.height));
            User.getInstance().setProfileImageUrl(imageUrl);
        }
        userProfileBinding.executePendingBindings();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                data.getData();
                CameraUtils.onSelectFromGalleryResult(getContext(), data, userProfileBinding.userAvatar);
            } else if (requestCode == EditProfileViewModel.REQUEST_CAMERA) {
                CameraUtils.onCaptureImageResult(getContext(), userProfileBinding.userAvatar);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.userChoosenTask.equals(getString(R.string.take_photo)))
                        CameraUtils.cameraIntent(getContext());
                    else if (CameraUtils.userChoosenTask.equals(getString(R.string.choose_from_library)))
                        CameraUtils.galleryIntent(getContext());
                } else {
                }
                break;
        }
    }

    public void selectImage() {
        CameraUtils.selectImage(getContext());
    }
}
