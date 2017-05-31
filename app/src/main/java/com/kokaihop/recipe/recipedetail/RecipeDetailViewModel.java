package com.kokaihop.recipe.recipedetail;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.kokaihop.base.BaseViewModel;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {


    public RecipeDetailViewModel() {
    }


    public void actionOnBack(View view) {
        ((Activity) view.getContext()).onBackPressed();
    }

    public void actionOnLeftSlider(ImageView view, final ViewPager viewPager) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

    }

    public void actionOnRightSlider(ImageView view, final ViewPager viewPager) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });

    }

    @Override
    protected void destroy() {
    }
}
