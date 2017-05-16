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
            /*case 1:
//                MainCourseFragment mainCourseFragment1 = MainCourseFragment.newInstance();
                return mainCourseFragment1;
            case 2:
                MainCourseFragment mainCourseFragment2 = MainCourseFragment.newInstance();
                return mainCourseFragment2;
            case 3:
                MainCourseFragment mainCourseFragment3 = MainCourseFragment.newInstance();
                return mainCourseFragment3;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}