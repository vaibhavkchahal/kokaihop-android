package com.kokaihop.recipe.recipedetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseActivity;

public class RecipeDetailActivity extends BaseActivity {

    private ImageView leftNav;
    private ImageView rightNav;
    private TextView[] dots;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_detail_toolbar);
        toolbar.setLogo(R.drawable.ic_back_arrow_md);
        setSupportActionBar(toolbar);
        leftNav = (ImageView) findViewById(R.id.viewpager_swipe_left);
        rightNav = (ImageView) findViewById(R.id.viewpager_swipe_right);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        viewPager = (ViewPager) findViewById(R.id.viewpager_recipe_detail);
        viewPager.setAdapter(new RecipeDetailPagerAdapter(this));

        addBottomDots(0);

        // Images left navigation
        leftNav.setOnClickListener(new View.OnClickListener() {
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
        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[viewPager.getAdapter().getCount()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
//            dots[i].getLayoutParams().height = 6;

            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#E8E8E8"));
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#06AAE4"));
    }
}
