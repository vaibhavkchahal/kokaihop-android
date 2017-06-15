package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityRecipeDetailBinding;
import com.altaworks.kokaihop.ui.databinding.DialogPortionBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.customviews.AppBarStateChangeListener;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.BlurImageHelper;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.ShareContents;
import com.kokaihop.utility.SharedPrefUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.altaworks.kokaihop.ui.BuildConfig.SERVER_BASE_URL;

public class RecipeDetailActivity extends BaseActivity implements RecipeDetailViewModel.DataSetListener {

    private int portionMinValue = 1;
    private int portionMaxValue = 79;

    private ViewPager viewPager;
    private ActivityRecipeDetailBinding binding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private TextView txtviewPagerProgress;
    private RecipeDetailRecyclerAdapter recyclerAdapter;
    private BottomSheetDialog portionDialog;
    private int quantityOriginal;
    private RecipeDetailPagerAdapter recipeDetailPagerAdapter;

    private final Observable.OnPropertyChangedCallback propertyChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            RecipeDetailActivity.this.invalidateOptionsMenu();
        }
    };
    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        recipeID = getIntent().getStringExtra("recipeId");
        txtviewPagerProgress = binding.txtviewPagerProgress;
        recipeDetailViewModel = new RecipeDetailViewModel(this, recipeID, this);
        binding.setViewModel(recipeDetailViewModel);
        setProfileImage();
        setToolbar();
        initializeViewPager();
        initializePagerLeftRightSlider();
        initializeRecycleView();
        setPagerData();
        setAppBarListener();
    }

    private void setProfileImage() {
        int profileImageSize = getResources().getDimensionPixelOffset(R.dimen.recipe_detail_header_profile_img_height_width);
        String profileImageUrl = CloudinaryUtils.getRoundedImageUrl(recipeDetailViewModel.getRecipeImageId(), String.valueOf(profileImageSize), String.valueOf(profileImageSize));
        binding.setProfileImageUrl(profileImageUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.getViewModel().addOnPropertyChangedCallback(propertyChangedCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.getViewModel().removeOnPropertyChangedCallback(propertyChangedCallback);
    }

    private void setAppBarListener() {
        AppBarLayout appBarLayout = binding.appbarLayout;
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                View viewCollapsed = binding.viewpagerRecipeDetail.getChildAt(binding.viewpagerRecipeDetail.getCurrentItem());
                if (viewCollapsed != null) {
                    ImageView imageViewRecipe = (ImageView) viewCollapsed.findViewById(R.id.imageview_recipe_pic);
                    ImageView imageViewBlurred = (ImageView) viewCollapsed.findViewById(R.id.imageview_recipe_blurred_pic);
                    switch (state) {
                        case COLLAPSED:
                            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
                            binding.viewpagerSwipeRight.setVisibility(View.GONE);
                            Bitmap bitmap = BlurImageHelper.captureView(imageViewRecipe);
                            Bitmap bluredBitmap = BlurImageHelper.blurBitmapWithRenderscript(
                                    RenderScript.create(RecipeDetailActivity.this),
                                    bitmap);
                            imageViewBlurred.setImageBitmap(bluredBitmap);
                            imageViewBlurred.setVisibility(View.VISIBLE);
                            break;
                        case EXPANDED:
                            toggleLeftRightVisibility(viewPager.getCurrentItem());
                            imageViewBlurred.setVisibility(View.INVISIBLE);

                            break;
                        case SCROLL_DOWN:
                            imageViewBlurred.setVisibility(View.INVISIBLE);
                            break;
                    }
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

    private void initializeViewPager() {
        viewPager = binding.viewpagerRecipeDetail;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toggleLeftRightVisibility(position);

                txtviewPagerProgress.setText(position + 1 + "/" + recipeDetailViewModel.getPagerImages().size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void toggleLeftRightVisibility(int position) {
        if (position == 0) {
            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
            binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);

        } else if (position == recipeDetailViewModel.getPagerImages().size() - 1) {
            binding.viewpagerSwipeLeft.setVisibility(View.VISIBLE);
            binding.viewpagerSwipeRight.setVisibility(View.GONE);

        } else {
            binding.viewpagerSwipeLeft.setVisibility(View.VISIBLE);
            binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);
        }
    }

    private void initializePagerLeftRightSlider() {
        binding.viewpagerSwipeLeft.setOnClickListener(new View.OnClickListener() {
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
        binding.viewpagerSwipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });

    }

    private void setPagerData() {
        recipeDetailPagerAdapter = new RecipeDetailPagerAdapter(this, recipeDetailViewModel.getPagerImages());
        viewPager.setAdapter(recipeDetailPagerAdapter);
        viewPager.setOffscreenPageLimit(recipeDetailViewModel.getPagerImages().size());
        if (recipeDetailViewModel.getPagerImages().size() > 0) {
            txtviewPagerProgress.setText("1/" + recipeDetailViewModel.getPagerImages().size());
        }
        if (recipeDetailViewModel.getPagerImages().size() > 1) {
            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
            binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);
        } else {
            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
            binding.viewpagerSwipeRight.setVisibility(View.GONE);
        }
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final RecipeHandler recipeHandler = new RecipeHandler();
        RecipeRealmObject realmObject = binding.getViewModel().recipeRealmObject;
        final Recipe recipe = binding.getViewModel().getRecipe(realmObject);
        MenuItem menuItemLike = menu.findItem(R.id.icon_like);
        setInitialRecipeLikeState(recipe, menuItemLike);
        menuItemLike.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.icon_like) {
                    actionOnRecipeLike(item, recipe, recipeHandler);
                }
                return false;
            }
        });
        return true;
    }

    private void setInitialRecipeLikeState(Recipe recipe, MenuItem menuItemLike) {
        if (recipe.isFavorite) {
            menuItemLike.setIcon(R.drawable.ic_like_sm);
            menuItemLike.setChecked(recipe.isFavorite);
        } else {
            menuItemLike.setIcon(R.drawable.ic_unlike_sm);
            menuItemLike.setChecked(false);
        }
    }

    private void actionOnRecipeLike(MenuItem item, Recipe recipe, RecipeHandler recipeHandler) {
        String accessToken = SharedPrefUtils.getSharedPrefStringData(RecipeDetailActivity.this, Constants.ACCESS_TOKEN);
        if (accessToken != null && !accessToken.isEmpty()) {
            if (item.isChecked()) {
                item.setIcon(R.drawable.ic_unlike_sm);
                item.setChecked(false);

            } else {
                item.setIcon(R.drawable.ic_like_sm);
                item.setChecked(true);
            }
        }
        CheckBox checkBox = binding.getViewModel().getCheckBox();
        checkBox.setChecked(item.isChecked());
        recipeHandler.onCheckChangeRecipe(checkBox, recipe);
        recipeHandler.setRecipePosition(getIntent().getIntExtra("recipePosition", -1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_share:
                Logger.e("Share Picture", "Menu");
                if (recipeDetailPagerAdapter.getCount() > 0) {

                    // Save this bitmap to a file.
                    File cache = getApplicationContext().getExternalCacheDir();
                    File sharefile = new File(cache, "recipe.jpg");
                    Log.d("share file type is", sharefile.getAbsolutePath());
                    try {
                        FileOutputStream out = new FileOutputStream(sharefile);
                        View viewCollapsed = binding.viewpagerRecipeDetail.getChildAt(binding.viewpagerRecipeDetail.getCurrentItem());
                        ImageView imageViewRecipe = (ImageView) viewCollapsed.findViewById(R.id.imageview_recipe_pic);
                        Bitmap bitmap = BlurImageHelper.captureView(imageViewRecipe);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("ERROR", String.valueOf(e.getMessage()));

                    }
                    ShareContents shareContents = new ShareContents(RecipeDetailActivity.this);
                    shareContents.setRecipeLink(SERVER_BASE_URL + "recept/" + recipeDetailViewModel.getRecipeFriendlyUrl());
                    shareContents.setRecipeTitle(recipeDetailViewModel.getRecipeTitle());
                    shareContents.setImageFile(sharefile);
                    shareContents.share();


//                    CameraUtils.sharePicture(this, imageUrl);
                }
                return true;
            case R.id.icon_camera:
                Logger.e("Add Picture", "Menu");
                return true;
            case R.id.icon_add_to_wishlist:
                Logger.e("Add to wishlist", "Menu");
                binding.getViewModel().openCookBookScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPagerDataUpdate() {
        setPagerData();
    }

    @Override
    public void onRecipeDetailDataUpdate() {

        binding.recyclerViewRecipeDetail.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onCounterUpdate() {
        if (viewPager.getAdapter().getCount() > 0) {
            binding.txtviewPagerProgress.setText("1/" + recipeDetailViewModel.getPagerImages().size());

        }

    }
}
