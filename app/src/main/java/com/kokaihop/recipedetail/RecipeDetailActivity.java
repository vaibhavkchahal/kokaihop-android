package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityRecipeDetailBinding;
import com.kokaihop.base.BaseActivity;

import io.realm.Realm;

public class RecipeDetailActivity extends BaseActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private Realm realm;
    private ActivityRecipeDetailBinding binding;
    private RecipeDetailViewModel recipeDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        String recipeID = getIntent().getStringExtra("recipeId");
        recipeDetailViewModel=new RecipeDetailViewModel(recipeID,binding.recyclerViewRecipeDetail);
        binding.setViewModel(recipeDetailViewModel);
//        getRecipeObject(binding);
        setToolbar();
        initializeViewPager();
        initializeRecycleView();

    }

    private void initializeRecycleView() {
        RecyclerView recyclerViewRecipeDetail = binding.recyclerViewRecipeDetail;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecipeDetailRecyclerAdapter recyclerAdapter = new RecipeDetailRecyclerAdapter(recipeDetailViewModel.getRecipeDetailItemsList());
        recyclerViewRecipeDetail.setLayoutManager(layoutManager);
        recyclerViewRecipeDetail.setAdapter(recyclerAdapter);
    }

    private void initializeViewPager() {
        ImageView leftSlider = binding.viewpagerSwipeLeft;
        ImageView rightSlider = binding.viewpagerSwipeRight;
        dotsLayout = binding.layoutDots;
        viewPager = binding.viewpagerRecipeDetail;
        viewPager.setAdapter(new RecipeDetailPagerAdapter(this));
        addDotsToPager(0);
        enablePagerLeftRightSlider(leftSlider, rightSlider);
    }

  /*  private void getRecipeObject(ActivityRecipeDetailBinding binding) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo("_id", getIntent().getStringExtra("recipeId")).findFirst();
        binding.setRecipe(recipeRealmObject);
        realm.commitTransaction();
    }*/

    private void setToolbar() {
        Toolbar toolbar = binding.recipeDetailToolbar;
        binding.imgviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addDotsToPager(int currentPage) {
        TextView[] dots = new TextView[viewPager.getAdapter().getCount()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.white_FFEEEEEE));
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.orange_FFF75A15));
    }


    private void enablePagerLeftRightSlider(ImageView leftSlide, ImageView rightSlide) {
        // Images left navigation
        leftSlide.setOnClickListener(new View.OnClickListener() {
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
        // Images right navigation
        rightSlide.setOnClickListener(new View.OnClickListener() {
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
                addDotsToPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
