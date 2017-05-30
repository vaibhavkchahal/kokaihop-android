package com.kokaihop.recipe.recipedetail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseActivity;

public class RecipeDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager_recipe_detail);
        viewPager.setAdapter(new RecipeDetailPagerAdapter(this));
    }
}
