package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityRecipeDetailBinding;
import com.altaworks.kokaihop.ui.databinding.DialogPortionBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.customviews.AppBarStateChangeListener;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeDetailPagerImages;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.FeedApiHelper;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import io.realm.Realm;

public class RecipeDetailActivity extends BaseActivity {

    private int portionMinValue = 1;
    private int portionMaxValue = 79;

    private ViewPager viewPager;
    private Realm realm;
    private ActivityRecipeDetailBinding binding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private TextView txtviewPagerProgress;
    private RecipeDetailRecyclerAdapter recyclerAdapter;
    private BottomSheetDialog portionDialog;
    private int quantityOriginal;
    private RecipeDetailPagerAdapter recipeDetailPagerAdapter;
    private RecipeDetailPagerImages recipeDetailPagerImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        String recipeID = getIntent().getStringExtra("recipeId");
        viewPager = binding.viewpagerRecipeDetail;
        txtviewPagerProgress = binding.txtviewPagerProgress;
        recipeDetailViewModel = new RecipeDetailViewModel(this, recipeID, binding.recyclerViewRecipeDetail, viewPager, txtviewPagerProgress);
        binding.setViewModel(recipeDetailViewModel);
        int profileImageSize = getResources().getDimensionPixelOffset(R.dimen.recipe_detail_header_profile_img_height_width);
        String profileImageUrl = CloudinaryUtils.getRoundedImageUrl(recipeDetailViewModel.getRecipeImageId(), String.valueOf(profileImageSize), String.valueOf(profileImageSize));
        binding.setProfileImageUrl(profileImageUrl);
        setToolbar();
        initializeViewPager(recipeID);
        initializeRecycleView();
        setAppBarListener();


    }

    private void setAppBarListener() {
        AppBarLayout appBarLayout = binding.appbarLayout;

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());

                switch (state) {
                    case COLLAPSED:
                        binding.viewpagerSwipeLeft.setVisibility(View.GONE);
                        binding.viewpagerSwipeRight.setVisibility(View.GONE);


                        break;
                    case EXPANDED:
                        binding.viewpagerSwipeLeft.setVisibility(View.VISIBLE);
                        binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);

                }
            }
        });

    }


    private void initializeRecycleView() {
        RecyclerView recyclerViewRecipeDetail = binding.recyclerViewRecipeDetail;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecipeDetailRecyclerAdapter(recipeDetailViewModel.getRecipeDetailItemsList());
        recyclerViewRecipeDetail.setLayoutManager(layoutManager);
        recyclerAdapter.setPortionClickListener(new RecipeDetailRecyclerAdapter.PortionClickListener() {
            @Override
            public void onPortionClick(int quantity) {
                showPortionDialog(quantity);

            }
        });
        recyclerViewRecipeDetail.setAdapter(recyclerAdapter);
    }

    private void showPortionDialog(final int quantity) {
        if (portionDialog == null) {
            quantityOriginal = quantity;
        }
        DialogPortionBinding portionBinding = DataBindingUtil.
                inflate(LayoutInflater.from(RecipeDetailActivity.this), R.layout.dialog_portion, (ViewGroup) binding.getRoot(), false);
        portionDialog = setDialogConfigration(portionBinding);
        final NumberPicker numberPicker = portionBinding.numberPickerPortion;
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(portionMinValue);
        numberPicker.setMaxValue(portionMaxValue);
        numberPicker.setValue(quantityOriginal);
        TextView doneTextView = portionBinding.textviewDone;
        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedValue = numberPicker.getValue();
                for (Object object : recipeDetailViewModel.getRecipeDetailItemsList()
                        ) {
                    if (object instanceof IngredientsRealmObject) {
                        IngredientsRealmObject ingredientsRealmObject = (IngredientsRealmObject) object;
                        ingredientsRealmObject.setAmount((ingredientsRealmObject.getAmount() / quantity) * selectedValue);

                    }
                    if (object instanceof RecipeQuantityVariator) {
                        RecipeQuantityVariator recipeQuantityVariator = (RecipeQuantityVariator) object;
                        recipeQuantityVariator.setQuantity(selectedValue);
                    }
                }
                portionDialog.dismiss();
                recyclerAdapter.notifyDataSetChanged();

            }
        });
        portionDialog.show();


    }

    private BottomSheetDialog setDialogConfigration(DialogPortionBinding portionBinding) {
        BottomSheetDialog dialog = new BottomSheetDialog(RecipeDetailActivity.this);
        dialog.setContentView(portionBinding.getRoot());
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        //Grab the window of the portionDialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the portionDialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    private void initializeViewPager(String recipeID) {
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
        ImageView leftSlider = binding.viewpagerSwipeLeft;
        ImageView rightSlider = binding.viewpagerSwipeRight;
        recipeDetailPagerAdapter = new RecipeDetailPagerAdapter(this, recipeRealmObject.getImages());
        viewPager.setAdapter(recipeDetailPagerAdapter);
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
        return true;
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
//                setCollapsingToolbarImage(recipeDetailPagerAdapter.getImageUrl(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setCollapsingToolbarImage(String imageUrl) {

        CollapsingToolbarLayout collapsingToolbarLayout = binding.collapsingToolbarLayout;

//        Glide.with(collapsingToolbarLayout.getContext()).load(url).placeholder(R.color.colorPrimary).into(binding.imageviewToolbarImage);

       /* collapsingToolbarLayout.setContentScrim(
                context.getResources()
                        .getDrawable(R.drawable.something);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icon_share:
                Logger.e("Share Picture", "Menu");
                if(recipeDetailPagerAdapter.getCount()>0){
                    String imageUrl = recipeDetailPagerAdapter.getImageUrl(viewPager.getCurrentItem());
//                    CameraUtils.sharePicture(this,imageUrl);
                }
                return true;
            case R.id.icon_camera:
                Logger.e("Add Picture", "Menu");
                return true;
            case R.id.icon_like:
                Logger.e("Like Recipe", "Menu");
                String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(this,Constants.ACCESS_TOKEN);
                return true;
            case R.id.icon_add_to_wishlist:
                Logger.e("Add to wishlist", "Menu");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
