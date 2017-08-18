package com.kokaihop.recipedetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityRecipeDetailBinding;
import com.altaworks.kokaihop.ui.databinding.DialogPortionBinding;
import com.altaworks.kokaihop.ui.databinding.ShareDialogBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.cookbooks.CookbooksDataManager;
import com.kokaihop.customviews.AppBarStateChangeListener;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.editprofile.EditProfileViewModel;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.home.ShoppingDataManager;
import com.kokaihop.userprofile.ConfirmImageUploadActivity;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.BlurImageHelper;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.ConfirmationDialog;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.RecipeUtils;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.altaworks.kokaihop.ui.BuildConfig.SERVER_BASE_URL;
import static com.kokaihop.KokaihopApplication.getContext;
import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;
import static com.kokaihop.utility.Constants.ACCESS_TOKEN;
import static com.kokaihop.utility.Constants.CONFIRM_REQUEST_CODE;
import static com.kokaihop.utility.SharedPrefUtils.getSharedPrefStringData;

public class RecipeDetailActivity extends BaseActivity implements RecipeDetailViewModel.DataSetListener, ShareAdapter.ShareItemClickListener {

    private int portionMinValue = 1;
    private int portionMaxValue = 79;
    private int NUMBER_OF_COLUMNS_IN_SHARE_GRID = 2;

    private Uri imageUri;
    private String filePath;
    private ViewPager viewPager;
    private ActivityRecipeDetailBinding binding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private TextView txtviewPagerProgress;
    private RecipeDetailRecyclerAdapter recyclerAdapter;
    private RecipeHandler recipeHandler;
    private BottomSheetDialog portionDialog;
    private int quantityOriginal;
    private RecipeDetailPagerAdapter recipeDetailPagerAdapter;

    private final Observable.OnPropertyChangedCallback propertyChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            RecipeDetailActivity.this.invalidateOptionsMenu();
        }
    };
    private String recipeID, userFriendlyUrl;
    private String comingFrom = "commentsSection";
    private String friendlyUrl;
    private String from;
    private Menu menu;
    private int currentPagerPosition = 1;
    private Dialog shareDialog;
    private RecipeRealmObject recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userFriendlyUrl = SharedPrefUtils.getSharedPrefStringData(this, Constants.FRIENDLY_URL);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        recipeID = getIntent().getStringExtra("recipeId");
        friendlyUrl = getIntent().getStringExtra("friendlyUrl");
        from = getIntent().getStringExtra("from");
        if (from != null && from.equalsIgnoreCase("Notification")) {
            GoogleAnalyticsHelper.trackEventAction(getString(R.string.pushnotification_category), getString(R.string.pushnotification_launched_action));

        }
        txtviewPagerProgress = binding.txtviewPagerProgress;
        setupRecipeDetailScreen();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.recipe_detail_screen));
        enableCoachMark();
    }

    private void enableCoachMark() {
        String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), ACCESS_TOKEN);
        boolean coachMarkVisibilty = SharedPrefUtils.getSharedPrefBooleanData(getContext(), Constants.RECIPE_DETAIL_COACHMARK_VISIBILITY);
        if (accessToken != null && !accessToken.isEmpty() && !coachMarkVisibilty) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View coachMarkView = inflater.inflate(R.layout.recipe_detail_coach_mark, null);
            AppUtility.showCoachMark(coachMarkView, Constants.RECIPE_DETAIL_COACHMARK_VISIBILITY);
        }
    }

    public void setupRecipeDetailScreen() {
        recipeDetailViewModel = new RecipeDetailViewModel(this, recipeID, friendlyUrl, this);
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
        EventBus.getDefault().register(this);
        binding.getViewModel().updateComments();
        binding.getViewModel().addOnPropertyChangedCallback(propertyChangedCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
                            if (imageViewRecipe.getMeasuredHeight() > 0 && imageViewRecipe.getMeasuredWidth() > 0) {
                                Bitmap bitmap = BlurImageHelper.captureView(imageViewRecipe);
                                Bitmap bluredBitmap = BlurImageHelper.blurBitmapWithRenderscript(
                                        RenderScript.create(RecipeDetailActivity.this),
                                        bitmap);
                                imageViewBlurred.setImageBitmap(bluredBitmap);
                                imageViewBlurred.setVisibility(View.VISIBLE);
                            }
                            changeMenuItemsIcons(true);
                            break;
                        case EXPANDED:
                            toggleLeftRightVisibility(viewPager.getCurrentItem());
                            imageViewBlurred.setVisibility(View.INVISIBLE);
                            changeMenuItemsIcons(false);
                            break;
                        case SCROLL_DOWN:
                            imageViewBlurred.setVisibility(View.INVISIBLE);
                            changeMenuItemsIcons(false);
                            break;
                        case SCROLL_UP:
                            changeMenuItemsIcons(true);
                    }
                } else {
                    switch (state) {
                        case SCROLL_DOWN:
                            changeMenuItemsIcons(false);
                            break;
                        case SCROLL_UP:
                            changeMenuItemsIcons(true);
                    }
                }
            }
        });
    }

    private void changeMenuItemsIcons(boolean collapsed) {
        if (menu != null) {
            MenuItem menuItemLike = menu.findItem(R.id.icon_like);
            MenuItem menuItemShare = menu.findItem(R.id.icon_share);
            MenuItem menuItemCamera = menu.findItem(R.id.icon_camera);
            MenuItem menuItemWishlist = menu.findItem(R.id.icon_add_to_wishlist);
            if (collapsed) {
                if (!recipe.isFavorite()) {
                    menuItemLike.setIcon(R.drawable.ic_like_md_grey);
                }
                menuItemShare.setIcon(R.drawable.ic_share_md_grey);
                menuItemCamera.setIcon(R.drawable.ic_camera_grey);
                menuItemWishlist.setIcon(R.drawable.ic_bookmark_md_grey);
                binding.imgviewBack.setImageResource(R.drawable.ic_back_arrow_sm_grey);
            } else {
                if (!recipe.isFavorite()) {
                    menuItemLike.setIcon(R.drawable.ic_unlike_md);
                }
                menuItemShare.setIcon(R.drawable.ic_share_md);
                menuItemCamera.setIcon(R.drawable.ic_camera);
                menuItemWishlist.setIcon(R.drawable.ic_bookmark_md);
                binding.imgviewBack.setImageResource(R.drawable.ic_back_arrow_sm);
            }
        }
    }


    private void initializeRecycleView() {
        RecyclerView recyclerViewRecipeDetail = binding.recyclerViewRecipeDetail;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecipeDetailRecyclerAdapter(comingFrom, recipeDetailViewModel.getRecipeDetailItemsList(), recipe);
        recyclerViewRecipeDetail.setLayoutManager(layoutManager);
        recyclerAdapter.setPortionClickListener(new RecipeDetailRecyclerAdapter.PortionClickListener() {
            @Override
            public void onPortionClick(int quantity) {
                showPortionDialog(quantity);

            }
        });
        recyclerAdapter.setAddToListClickListener(new RecipeDetailRecyclerAdapter.AddToListClickListener() {
            @Override
            public void onAddToListClick() {
                addItemsToShoppingList();
                EventBus.getDefault().postSticky(new AddToListEvent());
                GoogleAnalyticsHelper.trackEventAction(getString(R.string.buy_list_category), getString(R.string.buy_list_added_action), getString(R.string.buy_list_recipe_label));

            }
        });
        recyclerViewRecipeDetail.setAdapter(recyclerAdapter);
    }

    private void addItemsToShoppingList() {
        // club similar recipe ingredient.
        Map<String, IngredientsRealmObject> recipeIngredientsMap = new HashMap<>();
        for (Object object : recipeDetailViewModel.getRecipeDetailItemsList()) {
            if (object instanceof IngredientsRealmObject) {
                IngredientsRealmObject ingredientsRealmObject = (IngredientsRealmObject) object;
                String ingredientKey = AppUtility.checkIfUnitExist(ingredientsRealmObject);
                if (recipeIngredientsMap.containsKey(ingredientKey)) {
                    IngredientsRealmObject mapIngredientObject = recipeIngredientsMap.get(ingredientKey);
                    mapIngredientObject.setAmount(ingredientsRealmObject.getAmount() + mapIngredientObject.getAmount());
                } else {
                    recipeIngredientsMap.put(ingredientKey, ingredientsRealmObject);
                }
            }
        }
        // club recipe ingredient with similar ingredient in shopping list.
        ShoppingDataManager shoppingDataManager = new ShoppingDataManager();
        shoppingDataManager.addRecipeIngredientToShoppingList(recipeIngredientsMap);
        AppUtility.showAutoCancelMsgDialog(this, recipeIngredientsMap.size() + getString(R.string.item_added_text));
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
        AppUtility.setDividerHeight(portionBinding.numberPickerPortion, Constants.NUMBER_PICKER_HEIGHT);

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
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                // This is gotten directly from the source of BottomSheetDialog
                // in the wrapInBottomSheet() method
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                // Right here!
                BottomSheetBehavior.from(bottomSheet)
                        .setPeekHeight(Constants.BOTTOM_SHET_DIALOG_PEEK_HEIGHT);
            }
        });
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
            if (recipeDetailViewModel.getPagerImages().size() > 1) {
                binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);
            }

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

    private void    setPagerData() {
        if (recipeDetailPagerAdapter == null) {
            recipeDetailPagerAdapter = new RecipeDetailPagerAdapter(this, recipeDetailViewModel.getPagerImages());
            viewPager.setAdapter(recipeDetailPagerAdapter);
        } else {
            recipeDetailPagerAdapter.notifyDataSetChanged();
        }
        viewPager.setOffscreenPageLimit(recipeDetailViewModel.getPagerImages().size());
        if (recipeDetailViewModel.getPagerImages().size() > 0) {
            txtviewPagerProgress.setText(currentPagerPosition + "/" + recipeDetailViewModel.getPagerImages().size());
        }
        if (recipeDetailViewModel.getPagerImages().size() > 1) {
            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
            binding.viewpagerSwipeRight.setVisibility(View.VISIBLE);
        } else {
            binding.viewpagerSwipeLeft.setVisibility(View.GONE);
            binding.viewpagerSwipeRight.setVisibility(View.GONE);
        }
        if (recipeDetailViewModel.getPagerImages().size() == 0) {
            binding.recipeDetailPlaceholder.setVisibility(View.VISIBLE);
        } else {
            binding.recipeDetailPlaceholder.setVisibility(View.GONE);
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
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final Context context = this;
        final RecipeHandler recipeHandler = new RecipeHandler();
        final RecipeRealmObject recipeRealmObject = binding.getViewModel().recipeRealmObject;
//        final Recipe recipe = binding.getViewModel().getRecipe(realmObject);
        MenuItem menuItemLike = menu.findItem(R.id.icon_like);
        setInitialRecipeLikeState(recipeRealmObject, menuItemLike);
        menuItemLike.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.icon_like) {
                    String accessToken = getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                    if (accessToken == null || accessToken.isEmpty()) {
                        AppUtility.showLoginDialog(context, getString(R.string.members_area), getString(R.string.login_like_message));
                    } else {
                        actionOnRecipeLike(item, recipeRealmObject, recipeHandler);
                    }
                }
                return false;
            }
        });
        return true;
    }

    private void setInitialRecipeLikeState(RecipeRealmObject recipe, MenuItem menuItemLike) {
        this.recipe = recipe;
        if (recipe.isFavorite) {
            menuItemLike.setIcon(R.drawable.ic_like_sm);
            menuItemLike.setChecked(recipe.isFavorite);
        } else {
            menuItemLike.setIcon(R.drawable.ic_unlike_md);
            menuItemLike.setChecked(false);
        }
    }

    private void actionOnRecipeLike(final MenuItem item, final RecipeRealmObject recipe, final RecipeHandler recipeHandler) {
        this.recipeHandler = recipeHandler;
        String accessToken = getSharedPrefStringData(RecipeDetailActivity.this, Constants.ACCESS_TOKEN);
        if (accessToken != null && !accessToken.isEmpty()) {
            if (item.isChecked()) {
                if (recipeExistsInAnyCookbook(recipe.get_id())) {
                    final ConfirmationDialog dialog = new ConfirmationDialog(this,
                            getString(R.string.cookbook),
                            getString(R.string.recipe_unlike_warning),
                            getString(R.string.remove),
                            getString(R.string.cancel));
                    dialog.getConfirmPositive().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.setIcon(R.drawable.ic_unlike_sm);
                            item.setChecked(false);
                            updateCheckbox(recipeHandler, item, recipe);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    item.setIcon(R.drawable.ic_unlike_sm);
                    item.setChecked(false);
                    updateCheckbox(recipeHandler, item, recipe);
                }
            } else {
                item.setIcon(R.drawable.ic_like_sm);
                item.setChecked(true);
                updateCheckbox(recipeHandler, item, recipe);
            }
        }
    }

    public void updateCheckbox(RecipeHandler recipeHandler, MenuItem item, RecipeRealmObject recipe) {
        new CookbooksDataManager().removeRecipeFromAllCookbooks(userFriendlyUrl, recipe);
        CheckBox checkBox = binding.getViewModel().getCheckBox();
        checkBox.setChecked(item.isChecked());
        if (recipeHandler != null) {
            recipeHandler.onCheckChangeRecipe(checkBox, recipe);
            recipeHandler.setRecipePosition(getIntent().getIntExtra("recipePosition", -1));
        }
    }

    //    checks whether a recipe exists in any of the cookbook of user
    public boolean recipeExistsInAnyCookbook(String recipeId) {
        for (Cookbook cookbook : User.getInstance().getCookbooks()) {
            if (!cookbook.getFriendlyUrl().equals(Constants.FAVORITE_RECIPE_FRIENDLY_URL)) {
                if (RecipeUtils.getRecipeIndexInCookbook(userFriendlyUrl, cookbook.getFriendlyUrl(), recipeId) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_share:
                Logger.e("Share Picture", "Menu");
                List<Object> shareObjectsList = addShareOptions();
                ShareUsingPrint shareUsingPrint = prepareContentToPrint();
                shareObjectsList.add(shareUsingPrint);
                final ShareAdapter shareAdapter = new ShareAdapter(this, shareObjectsList, this);
                shareAdapter.setRecipeLink(SERVER_BASE_URL + "recept/" + recipeDetailViewModel.getRecipeFriendlyUrl());
                shareAdapter.setRecipeTitle(recipeDetailViewModel.getRecipeTitle());
                if (recipeDetailPagerAdapter != null && recipeDetailPagerAdapter.getCount() > 0) {
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
                        shareAdapter.setShareFile(sharefile);
                    } catch (IOException e) {
                        Log.e("ERROR", String.valueOf(e.getMessage()));

                    }
                }
//                    ShareContents shareContents = new ShareContents(RecipeDetailActivity.this);
//                    shareContents.setRecipeLink(SERVER_BASE_URL + "recept/" + recipeDetailViewModel.getRecipeFriendlyUrl());
//                    shareContents.setRecipeTitle(recipeDetailViewModel.getRecipeTitle());
//                    shareContents.setImageFile(sharefile);
//                    shareContents.share();
//                    CameraUtils.sharePicture(this, imageUrl);
                // Create alert shareDialog box
                shareDialog = new Dialog(this);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ShareDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.share_dialog, null, false);
                shareDialog.setContentView(binding.getRoot());
                binding.recyclerviewShare.setAdapter(shareAdapter);
                GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_SHARE_GRID);
                binding.recyclerviewShare.setLayoutManager(layoutManager);
                shareDialog.setCanceledOnTouchOutside(true);
                shareDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                shareDialog.show();
                return true;
            case R.id.icon_camera:
                recipeDetailViewModel.getRecipeDetails();
                String accessToken = getSharedPrefStringData(this, Constants.ACCESS_TOKEN);
                if (accessToken == null || accessToken.isEmpty()) {
                    AppUtility.showLoginDialog(this, getString(R.string.members_area), getString(R.string.login_upload_pic_message));
                } else {
                    CameraUtils.selectImage(this);
                }
                return true;
            case R.id.icon_add_to_wishlist:
                accessToken = getSharedPrefStringData(this, Constants.ACCESS_TOKEN);
                if (accessToken == null || accessToken.isEmpty()) {
                    AppUtility.showLoginDialog(this, getString(R.string.members_area), getString(R.string.login_add_to_cookbook_message));
                } else {
                    this.binding.getViewModel().openCookBookScreen();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @NonNull
    private List<Object> addShareOptions() {
        List<Object> shareObjectsList = new ArrayList<>();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        // what type of data needs to be send by sharing
        sharingIntent.setType("text/plain");
        // package names
        PackageManager pm = getPackageManager();
        // list package
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (ResolveInfo resolveInfo : activityList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (packageName.equals("com.twitter.android") || packageName.equals("com.facebook.katana") || packageName.equals("com.android.mms") || packageName.equals("com.android.messaging") || packageName.equals("com.google.android.gm")) {
                shareObjectsList.add(resolveInfo);
            }
        }
        return shareObjectsList;
    }

    @NonNull
    private ShareUsingPrint prepareContentToPrint() {
        ShareUsingPrint shareUsingPrint = new ShareUsingPrint(getString(R.string.text_print), R.drawable.ic_feed_orange_sm);
        String ingredients = "";
        String directions = "";
        for (Object object : recipeDetailViewModel.getRecipeDetailItemsList()) {
            if (object instanceof RecipeDetailHeader) {
                RecipeDetailHeader detailHeader = (RecipeDetailHeader) object;
                shareUsingPrint.setRecipeDescription(detailHeader.getDescription());
            } else if (object instanceof IngredientsRealmObject) {
                IngredientsRealmObject ingredient = (IngredientsRealmObject) object;
                if (ingredient.getAmount() > 0) {
                    ingredients = ingredients + ingredient.getAmount() + " " + ingredient.getUnit().getName() + " " + ingredient.getName() + "<br>";
                } else {
                    ingredients = ingredients + ingredient.getName() + "<br>";
                }
            } else if (object instanceof RecipeCookingDirection) {
                RecipeCookingDirection stepDetail = (RecipeCookingDirection) object;
                directions = directions + stepDetail.getDirection().getSerialNo() + ".  " + stepDetail.getDirection().getStep() + "<br>";
            }
        }
        shareUsingPrint.setIngredients(ingredients);
        shareUsingPrint.setDirections(directions);
        return shareUsingPrint;
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
        currentPagerPosition = viewPager.getCurrentItem() + 1;
        if (viewPager.getAdapter().getCount() > 0) {
            binding.txtviewPagerProgress.setText(currentPagerPosition + "/" + recipeDetailViewModel.getPagerImages().size());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.userChoosenTask.equals(getString(R.string.take_photo)))
                        CameraUtils.cameraIntent(this);
                    else if (CameraUtils.userChoosenTask.equals(getString(R.string.choose_from_library)))
                        CameraUtils.galleryIntent(this);
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditProfileViewModel.REQUEST_GALLERY || requestCode == EditProfileViewModel.REQUEST_CAMERA) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                if (data != null) {
                    imageUri = data.getData();
                    filePath = CameraUtils.getRealPathFromURI(RecipeDetailActivity.this, imageUri);
                    Intent confirmIntent = new Intent(this, ConfirmImageUploadActivity.class);
                    confirmIntent.setData(imageUri);
                    startActivityForResult(confirmIntent, CONFIRM_REQUEST_CODE);
                }
            } else {
                filePath = CameraUtils.onCaptureImageResult();
                recipeDetailViewModel.uploadImageOnCloudinary(filePath);
            }
            Logger.d("File Path", filePath);
        } else if (requestCode == RecipeDetailViewModel.ADD_TO_COOKBOOK_REQ_CODE) {
            if (data != null) {
                MenuItem menuItemLike = menu.findItem(R.id.icon_like);
                boolean isFavorite = data.getBooleanExtra("favorite", false);
                menuItemLike.setChecked(isFavorite);
                if (isFavorite) {
                    menuItemLike.setIcon(R.drawable.ic_like_sm);
                }
            }
        } else if (requestCode == Constants.CONFIRM_REQUEST_CODE && resultCode == RESULT_OK) {
            recipeDetailViewModel.uploadImageOnCloudinary(filePath);
        } else if (requestCode == Constants.OPEN_USER_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
//            Toast.makeText(this, "getting profile here.", Toast.LENGTH_SHORT).show();
            recipeDetailViewModel.getRecipeDetails();
        }
    }


    @Subscribe(sticky = true)
    public void onEvent(String update) {
        if (update.equalsIgnoreCase("refreshRecipeDetail")) {
            setupRecipeDetailScreen();
        }
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onBackPressed() {
        if (from != null && from.equalsIgnoreCase("Notification")) {
            AppUtility.showHomeScreen(RecipeDetailActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onShareItemClick() {
        if (shareDialog != null) {
            shareDialog.dismiss();
        }
    }

    public void setRecipe(RecipeRealmObject recipe) {
        this.recipe = recipe;
    }
}
