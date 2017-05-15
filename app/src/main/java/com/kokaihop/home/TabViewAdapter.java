package com.kokaihop.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class TabViewAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public TabViewAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserFeedFragment();
            case 1:
                return new UserProfileFragment();
            case 2:
                return new UserFeedFragment();
            case 3:
                return new UserFeedFragment();
            case 4  :
                return new UserFeedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
