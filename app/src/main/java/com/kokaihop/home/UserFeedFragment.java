package com.kokaihop.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.feed.RecipeTabAdapter;

public class UserFeedFragment extends Fragment {
    static UserFeedFragment fragment;

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
        View rootView = inflater.inflate(R.layout.fragment_user_feed, container, false);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout_recipe);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_main_course_of_the_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_appetizer_of_the_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_cookie_of_the_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_dessert_of_the_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_vegetarian_of_the_day));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        final PagerAdapter adapter = new RecipeTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        return rootView;
    }
}
