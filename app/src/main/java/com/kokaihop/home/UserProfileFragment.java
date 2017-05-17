package com.kokaihop.home;

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
import com.altaworks.kokaihop.ui.databinding.ProfileTabBinding;
import com.altaworks.kokaihop.ui.databinding.TablayoutTabBinding;
import com.kokaihop.home.userprofile.ProfileAdapter;

public class UserProfileFragment extends Fragment {
    static UserProfileFragment fragment;
    FragmentUserProfileBinding userProfileBinding;
    String[] tabTitles = {"Recipes", "Followers", "Following"};
    ViewPager viewPager;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        final TabLayout tabLayout = userProfileBinding.tabProfile;
        viewPager = userProfileBinding.viewpagerProfile;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ProfileAdapter adapter = new ProfileAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < 3; i++) {
            ProfileTabBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.profile_tab, null, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.text1.setText("457");
            tabBinding.text2.setText(tabTitles[i]);
        }
        TablayoutTabBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tablayout_tab, null, false);
        View tabView = tabBinding.getRoot();
        tabLayout.getTabAt(3).setCustomView(tabView);
        tabBinding.text1.setText("History");
        tabBinding.text1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_history, 0, 0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FFF75A15"));
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(Color.parseColor("#FFF75A15"));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FFD4D4D4"));
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(Color.parseColor("#FFD4D4D4"));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FFF75A15"));
                if (tab.getCustomView().findViewById(R.id.text2) != null) {
                    ((TextView) tab.getCustomView().findViewById(R.id.text2)).setTextColor(Color.parseColor("#FFF75A15"));
                }
            }
        });
        tabLayout.getTabAt(0).select();
        View view = userProfileBinding.getRoot();
        return view;
    }
}
