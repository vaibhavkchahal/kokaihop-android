package com.kokaihop.home.userprofile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class ProfileAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public ProfileAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return RecipeFragment.getInstance();
            case 1:
                return FollowersFragment.getInstance();
            case 2:
                return FollowingFragment.getInstance();
            case 3:
                return HistoryFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
