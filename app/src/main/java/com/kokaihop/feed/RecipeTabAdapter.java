package com.kokaihop.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.kokaihop.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    public RecipeTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

   /* @Override
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
    }*/


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        Logger.i("list size", mFragmentList.size() + "");
    }
    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

  /*  *
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object*/

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
}