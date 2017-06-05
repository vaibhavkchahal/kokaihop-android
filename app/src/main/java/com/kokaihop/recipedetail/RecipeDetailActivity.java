package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityRecipeDetailBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;

import io.realm.Realm;

public class RecipeDetailActivity extends BaseActivity {

    private ViewPager viewPager;
    private Realm realm;
    private ActivityRecipeDetailBinding binding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private TextView txtviewPagerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        String recipeID = getIntent().getStringExtra("recipeId");
        viewPager = binding.viewpagerRecipeDetail;
        txtviewPagerProgress = binding.txtviewPagerProgress;
        recipeDetailViewModel = new RecipeDetailViewModel(recipeID, binding.recyclerViewRecipeDetail, viewPager,txtviewPagerProgress);
        binding.setViewModel(recipeDetailViewModel);
//        getRecipeObject(binding);
        setToolbar();
        initializeViewPager(recipeID);
        initializeRecycleView();

    }


    private void initializeRecycleView() {
        RecyclerView recyclerViewRecipeDetail = binding.recyclerViewRecipeDetail;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecipeDetailRecyclerAdapter recyclerAdapter = new RecipeDetailRecyclerAdapter(recipeDetailViewModel.getRecipeDetailItemsList());
        recyclerViewRecipeDetail.setLayoutManager(layoutManager);
        recyclerViewRecipeDetail.setAdapter(recyclerAdapter);
    }

    private void initializeViewPager(String recipeID) {
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(recipeID);
        ImageView leftSlider = binding.viewpagerSwipeLeft;
        ImageView rightSlider = binding.viewpagerSwipeRight;
        viewPager.setAdapter(new RecipeDetailPagerAdapter(this, recipeRealmObject.getImages()));
        txtviewPagerProgress.setText("1/" + recipeRealmObject.getImages().size());
        enablePagerLeftRightSlider(leftSlider, rightSlider);
    }

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
                txtviewPagerProgress.setText(position + 1 + "/" + viewPager.getAdapter().getCount());
//                addDotsToPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
