package com.kokaihop.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public RecipeTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MainCourseFragment mainCourseFragment = MainCourseFragment.newInstance();
                return mainCourseFragment;
            case 1:
                AppetizerFragment appetizerFragment = new AppetizerFragment();
                return appetizerFragment;
            case 2:
                CookieFragment cookieFragment = new CookieFragment();
                return cookieFragment;
            case 3:
                DessertFragment dessertFragment = new DessertFragment();
                return dessertFragment;
            case 4:
                VegetarianFragment vegetarianFragment = new VegetarianFragment();
                return vegetarianFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}