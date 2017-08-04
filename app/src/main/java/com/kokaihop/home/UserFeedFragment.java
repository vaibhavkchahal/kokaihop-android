package com.kokaihop.home;

import android.content.Intent;
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
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.feed.AppetizerFragment;
import com.kokaihop.feed.CookieFragment;
import com.kokaihop.feed.DessertFragment;
import com.kokaihop.feed.MainCourseFragment;
import com.kokaihop.feed.PagerTabAdapter;
import com.kokaihop.feed.VegetarianFragment;
import com.kokaihop.search.SearchActivity;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.utility.Constants.ACCESS_TOKEN;

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
    public void onResume() {
        super.onResume();
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
        showUserProfile();
        enableCoachMark(inflater);
        return userFeedBinding.getRoot();
    }

    private void enableCoachMark(LayoutInflater inflater) {
        boolean searchCoachMarkVisibilty = SharedPrefUtils.getSharedPrefBooleanData(getContext(), Constants.SEARCH_COACHMARK_VISIBILITY);
        String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), ACCESS_TOKEN);
//        if (accessToken != null && !accessToken.isEmpty() && !searchCoachMarkVisibilty) {
            View coachMarkView = inflater.inflate(R.layout.search_coach_mark, null);
            AppUtility.showCoachMark(coachMarkView);
//            SharedPrefUtils.setSharedPrefBooleanData(getContext(), Constants.SEARCH_COACHMARK_VISIBILITY, true);
//        }
    }


    public void showUserProfile() {
        userFeedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_feed, container, false);
        final TabLayout tabLayout = userFeedBinding.tablayoutRecipe;
        viewPager = userFeedBinding.pager;
        viewPager.setOffscreenPageLimit(5);
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
        adapter.addFrag(mainCourseFragment, tabTitles[0]);
        AppetizerFragment appetizerFragment = new AppetizerFragment().newInstance();
        adapter.addFrag(appetizerFragment, tabTitles[1]);
        CookieFragment cookieFragment = new CookieFragment().newInstance();
        adapter.addFrag(cookieFragment, tabTitles[2]);
        DessertFragment dessertFragment = new DessertFragment().newInstance();
        adapter.addFrag(dessertFragment, tabTitles[3]);
        VegetarianFragment vegetarianFragment = new VegetarianFragment().newInstance();
        adapter.addFrag(vegetarianFragment, tabTitles[4]);
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
        userFeedBinding.textviewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClick();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                trackGAEvent(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void trackGAEvent(int position) {
        String label = "";
        switch (position) {
            case 0:
                label = getString(R.string.main_course_label);
                break;
            case 1:
                label = getString(R.string.appetizer_label);
                break;
            case 2:
                label = getString(R.string.cookie_label);
                break;
            case 3:
                label = getString(R.string.dessert_label);
                break;
            case 4:
                label = getString(R.string.vegetarian_label);
                break;
        }
        GoogleAnalyticsHelper.trackEventAction(getString(R.string.daily_category), getString(R.string.daily_selected_action), label);


    }

    public void searchClick() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);

    }
}
