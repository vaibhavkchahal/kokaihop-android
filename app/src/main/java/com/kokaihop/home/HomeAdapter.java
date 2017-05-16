package com.kokaihop.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class HomeAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public HomeAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return UserFeedFragment.getInstance();
            case 1:
                return CookbooksFragment.getInstance();
            case 2:
                return ListFragment.getInstance();
            case 3:
                return CommentsFragment.getInstance();
            case 4  :
                return UserProfileFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
