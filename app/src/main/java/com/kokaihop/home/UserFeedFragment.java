package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentUserFeedBinding;
import com.altaworks.kokaihop.ui.databinding.TabFeedTabLayoutBinding;
import com.kokaihop.feed.RecipeTabAdapter;

public class UserFeedFragment extends Fragment {
    static UserFeedFragment fragment;
    FragmentUserFeedBinding userFeedBinding;
    ViewGroup container;
    LayoutInflater inflater;
    private ViewPager viewPager;


    public UserFeedFragment() {

    }

    public static UserFeedFragment getInstance() {
        if (fragment == null) {
            fragment = new UserFeedFragment();
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
        this.inflater = inflater;
        this.container = container;
        showUserProfile();
        return userFeedBinding.getRoot();
    }

    public void showUserProfile() {
        userFeedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_feed, container, false);
        final TabLayout tabLayout = userFeedBinding.tablayoutRecipe;
        viewPager = userFeedBinding.pager;
        String[] tabTitles = {getActivity().getString(R.string.tab_main_course_of_the_day),
                getActivity().getString(R.string.tab_appetizer_of_the_day),
                getActivity().getString(R.string.tab_cookie_of_the_day),
                getActivity().getString(R.string.tab_dessert_of_the_day),
                getActivity().getString(R.string.tab_vegetarian_of_the_day),};
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        final PagerAdapter adapter = new RecipeTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(tabLayout.getTabCount() );
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabFeedTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_feed_tab_layout, container, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.text1.setText(tabTitles[i]);
        }

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FF8E8E93"));
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FFD4D4D4"));
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                ((TextView) tab.getCustomView().findViewById(R.id.text1)).setTextColor(Color.parseColor("#FFF75A15"));
//            }
//        });

        tabLayout.getTabAt(0).select();
    }

}
