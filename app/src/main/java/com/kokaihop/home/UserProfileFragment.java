package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        TabLayout tabLayout = userProfileBinding.tabProfile;
        viewPager = userProfileBinding.viewpagerProfile;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ProfileAdapter adapter = new ProfileAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < 3; i++) {
            ProfileTabBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.profile_tab, null, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.text1.setText("457");
            tabBinding.tvTitle.setText(tabTitles[i]);
        }
        TablayoutTabBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tablayout_tab, null, false);
        View tabView = tabBinding.getRoot();
        TabLayout.Tab tab = tabLayout.getTabAt(3).setCustomView(tabView);
        tabBinding.text1.setText("History");
        tabBinding.text1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_history,0,0);

        View view = userProfileBinding.getRoot();
        return view;
    }
}
