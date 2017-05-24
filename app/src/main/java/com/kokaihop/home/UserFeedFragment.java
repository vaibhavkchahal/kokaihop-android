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
import com.altaworks.kokaihop.ui.databinding.FragmentUserFeedBinding;
import com.altaworks.kokaihop.ui.databinding.TabFeedTabLayoutBinding;
import com.kokaihop.feed.AppetizerFragment;
import com.kokaihop.feed.CookieFragment;
import com.kokaihop.feed.DessertFragment;
import com.kokaihop.feed.maincourse.MainCourseFragment;
import com.kokaihop.feed.PagerTabAdapter;
import com.kokaihop.feed.VegetarianFragment;

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

        final PagerTabAdapter adapter = new PagerTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        MainCourseFragment mainCourseFragment = MainCourseFragment.newInstance();
        adapter.addFrag(mainCourseFragment,tabTitles[0]);
        AppetizerFragment appetizerFragment = new AppetizerFragment().newInstance();
        adapter.addFrag(appetizerFragment,tabTitles[1]);
        CookieFragment cookieFragment = new CookieFragment();
        adapter.addFrag(cookieFragment,tabTitles[2]);
        DessertFragment dessertFragment = new DessertFragment();
        adapter.addFrag(dessertFragment,tabTitles[3]);
        VegetarianFragment vegetarianFragment = new VegetarianFragment();
        adapter.addFrag(vegetarianFragment,tabTitles[4]);
        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(tabLayout.getTabCount() );
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabFeedTabLayoutBinding tabBinding = DataBindingUtil.inflate(inflater, R.layout.tab_feed_tab_layout, container, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            tabBinding.text1.setText(tabTitles[i]);
        }

        tabLayout.getTabAt(0).select();
    }

}
