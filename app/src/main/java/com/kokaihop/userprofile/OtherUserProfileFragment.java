package com.kokaihop.userprofile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentUserProfileBinding;
import com.altaworks.kokaihop.ui.databinding.TabProfileTabLayoutBinding;
import com.altaworks.kokaihop.ui.databinding.TabProfileTabLayoutStvBinding;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.utility.Constants.ACCESS_TOKEN;

public class OtherUserProfileFragment extends Fragment implements UserApiCallback {
    private static OtherUserProfileFragment fragment;
    private FragmentUserProfileBinding userProfileBinding;
    UserProfileViewModel userViewModel;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private ViewGroup container;

    public OtherUserProfileFragment() {

    }

    public static OtherUserProfileFragment getInstance() {

        if (fragment == null) {
            fragment = new OtherUserProfileFragment();
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

        userProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_user_profile, container, false);
        userViewModel = new UserProfileViewModel(getContext(), this);
        userViewModel.getUserData();
        return userProfileBinding.getRoot();
    }

    @Override
    public void showUserProfile() {
        User user = User.getInstance();
        final TabLayout tabLayout = userProfileBinding.tabProfile;
        final int activeColor = Color.parseColor(getString(R.string.user_active_tab_text_color));
        final int inactiveColor = Color.parseColor(getString(R.string.user_inactive_tab_text_color));
        int tabCount = 4;
        int i;

        userProfileBinding.setUser(User.getInstance());

        String[] tabTitles = {getActivity().getString(R.string.tab_recipes),
                getActivity().getString(R.string.tab_cookbooks),
                getActivity().getString(R.string.tab_followers),
                getActivity().getString(R.string.tab_following)};
//        TODO: counts should be set here.

        int[] counts = {user.getRecipeCount(),
                user.getFollowers().size(),
                user.getFollowing().size()};

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
            tabBinding.text1.setText("" + counts[i]);
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

        tabLayout.getTabAt(0).select();
    }

    @Override
    public void followToggeled() {

    }
}
