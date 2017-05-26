package com.kokaihop.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.kokaihop.home.userprofile.FollowersFragment;
import com.kokaihop.home.userprofile.FollowingFragment;
import com.kokaihop.home.userprofile.HistoryFragment;
import com.kokaihop.home.userprofile.ProfileAdapter;
import com.kokaihop.home.userprofile.RecipeFragment;
import com.kokaihop.home.userprofile.UserApiCallback;
import com.kokaihop.home.userprofile.UserProfileViewModel;
import com.kokaihop.home.userprofile.UserSettingsActivity;
import com.kokaihop.home.userprofile.model.NotificationCount;
import com.kokaihop.home.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

import static com.kokaihop.utility.Constants.ACCESS_TOKEN;

public class UserProfileFragment extends Fragment implements UserApiCallback{
    private static UserProfileFragment fragment;
    private FragmentUserProfileBinding userProfileBinding;
    FragmentUserProfileSignUpBinding userProfileSignUpBinding;
    UserProfileViewModel userViewModel;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private ViewGroup container;
    private Point point;
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
            userProfileBinding.setViewModel(userViewModel);
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
                CustomDialogSignUp signUp = (CustomDialogSignUp) DialogFragment.instantiate(getActivity(),"com.kokaihop.home.CustomDialogSignUp");
                signUp.show(getFragmentManager(),"");
//                startActivity(new Intent(getContext(), SignUpActivity.class));
//                TODO:to be Checked
            }
        });
    }

    @Override
    public void showUserProfile() {
        User user = User.getInstance();
        final TabLayout tabLayout = userProfileBinding.tabProfile;
        final int activeColor = Color.parseColor(getString(R.string.user_active_tab_text_color));
        final int inactiveColor = Color.parseColor(getString(R.string.user_inactive_tab_text_color));
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
        ProfileAdapter adapter = new ProfileAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        adapter.addFrag(new RecipeFragment(),"Recipes");
        adapter.addFrag(new FollowersFragment(),"Followers");
        adapter.addFrag(new FollowingFragment(),"Following");
        adapter.addFrag(new HistoryFragment(),"History");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(tabCount);
        tabLayout.setupWithViewPager(viewPager);


        for (i = 0; i < (tabCount - 1); i++) {
            TabProfileTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_profile_tab_layout, null, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.setNotification(notificationCount.get(i));
//            tabBinding.text1.setText("" + counts[i]);
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
        userProfileBinding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserSettingsActivity.class));
            }
        });

        tabLayout.getTabAt(Constants.TAB_RECIPES).select();
    }

    @Override
    public void followToggeled() {

    }

    public void setNotificationCount() {
        User user = User.getInstance();
        notificationCount.get(Constants.TAB_RECIPES).setCount(user.getRecipeCount());
        notificationCount.get(Constants.TAB_FOLLOWERS).setCount(user.getFollowers().size());
        notificationCount.get(Constants.TAB_FOLLOWINGS).setCount(user.getFollowing().size());
    }

    public void setCoverImage(){
        point = AppUtility.getDisplayPoint(getContext());
        int width = point.x;
        float ratio = (float)195/320;
        int height = AppUtility.getHeightInAspectRatio(width,ratio);
        ImageView ivCover = userProfileBinding.ivProfileCover;
        CollapsingToolbarLayout collapsingToolbarLayout = userProfileBinding.collapsingToolbar;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivCover.setLayoutParams(layoutParams);

//        collapsingToolbarLayout.setl/a

        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivCover.getLayoutParams();
        userProfileBinding.setImageCoverUrl(CloudinaryUtils.getImageUrl(User.getInstance().getCoverImage().getCloudinaryId(),String.valueOf(coverLayoutParams.width),String.valueOf(coverLayoutParams.height)));
//        userProfileBinding.setImageCoverUrl(CloudinaryUtils.getImageUrl("35035757",String.valueOf(coverLayoutParams.width),String.valueOf(coverLayoutParams.height)));
        userProfileBinding.executePendingBindings();
    }

    public void setProfileImage(){
        int width = userProfileBinding.userAvatar.getWidth();
        int height = width;
        ImageView ivProfile = userProfileBinding.userAvatar;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivProfile.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams coverLayoutParams = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
        userProfileBinding.setImageProfileUrl(CloudinaryUtils.getRoundedImageUrl(User.getInstance().getProfileImage().getCloudinaryId(),String.valueOf(coverLayoutParams.width),String.valueOf(coverLayoutParams.height)));
        userProfileBinding.executePendingBindings();
    }
}
