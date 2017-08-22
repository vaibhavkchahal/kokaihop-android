package com.kokaihop.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityHomeBinding;
import com.altaworks.kokaihop.ui.databinding.TabHomeTabLayoutBinding;
import com.google.android.gms.ads.MobileAds;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.customviews.NonSwipeableViewPager;
import com.kokaihop.customviews.NotificationDialogActivity;
import com.kokaihop.editprofile.EditProfileViewModel;
import com.kokaihop.feed.PagerTabAdapter;
import com.kokaihop.recipedetail.AddToListEvent;
import com.kokaihop.userprofile.ConfirmImageUploadActivity;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;
import static com.kokaihop.utility.Constants.CONFIRM_REQUEST_CODE;
import static com.kokaihop.utility.Constants.TAB_COOKBOOKS;
import static com.kokaihop.utility.Constants.TAB_SHOPPING_LIST;
import static com.kokaihop.utility.Constants.TAB_USER_PROFILE;

public class HomeActivity extends BaseActivity {
    private NonSwipeableViewPager viewPager;
    private TabLayout tabLayout;
    private ActivityHomeBinding activityHomeBinding;
    private int tabCount = 5;
    private HomeViewModel viewModel;
    private UserProfileFragment userProfileFragment;
    private int[] activeTabsIcon = {
            R.drawable.ic_feed_orange_sm,
            R.drawable.ic_cookbooks_orange_sm,
            R.drawable.ic_list_orange_sm,
            R.drawable.ic_comments_orange_sm,
            R.drawable.ic_user_orange_sm
    };
    private int[] inactiveTabsIcon = {
            R.drawable.ic_feed_white_sm,
            R.drawable.ic_cookbooks_white_sm,
            R.drawable.ic_list_white_sm,
            R.drawable.ic_comments_white_sm,
            R.drawable.ic_user_white_sm
    };
    private NotificationReceiver notificationReciever;

    private Uri imageUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String useremailPwd = SharedPrefUtils.getSharedPrefStringData(this, Constants.USER_Email_PASSWORD);
        if (!useremailPwd.equals("")) {
            String email = useremailPwd.substring(0, useremailPwd.indexOf("~"));
            String password = useremailPwd.substring(useremailPwd.indexOf("~") + 1);
            Log.e("Email", "-" + email);
            Log.e("Password", "-" + password);
            viewModel.login(email, password);
        }
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        MobileAds.initialize(this, AppCredentials.ADMOB_APP_ID);
        viewModel = new HomeViewModel(this);
        viewModel.getLatestRecipes();
        setTabView();
        notificationReciever = new NotificationReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intent = new IntentFilter(Constants.SHOW_DIALOG_ACTION);
        registerReceiver(notificationReciever, intent);
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.daily_screen));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReciever);

    }

    public void setTabView() {
        tabLayout = activityHomeBinding.tabLayout;
        viewPager = activityHomeBinding.pager;
        viewPager.setPagingEnabled(false);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        final PagerTabAdapter adapter = new PagerTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        userProfileFragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, SharedPrefUtils.getSharedPrefStringData(this, Constants.USER_ID));
        bundle.putString(Constants.FRIENDLY_URL, SharedPrefUtils.getSharedPrefStringData(this, Constants.FRIENDLY_URL));
        adapter.addFrag(new UserFeedFragment(), getString(R.string.tab_feed));
        MyCookbooksFragment myCookbooksFragment = new MyCookbooksFragment();
        myCookbooksFragment.setArguments(bundle);
        adapter.addFrag(myCookbooksFragment, getString(R.string.tab_cookbooks));
        adapter.addFrag(new ShoppingListFragment(), getString(R.string.tab_list));
        adapter.addFrag(new CommentsFragment(), getString(R.string.tab_comments));
        adapter.addFrag(userProfileFragment, getString(R.string.tab_me));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        setTabTextIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedPosition = tabLayout.getSelectedTabPosition();
                ((TextView) tabLayout.getTabAt(selectedPosition)
                        .getCustomView()
                        .findViewById(R.id.text1))
                        .setCompoundDrawablesWithIntrinsicBounds(0, activeTabsIcon[selectedPosition], 0, 0);
                sendScreenName(selectedPosition);
                PagerTabAdapter pagerTabAdapter = (PagerTabAdapter) viewPager.getAdapter();
                if (selectedPosition == TAB_COOKBOOKS) {
                    MyCookbooksFragment fragment = (MyCookbooksFragment) pagerTabAdapter.getItem(TAB_COOKBOOKS);
                    fragment.refresh();
                } else if (selectedPosition == TAB_USER_PROFILE) {
                    UserProfileFragment fragment = (UserProfileFragment) pagerTabAdapter.getItem(TAB_USER_PROFILE);
                    fragment.setNotificationCount();
                    fragment.refreshHistory();
                    fragment.refreshFollowing();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                        .getCustomView()
                        .findViewById(R.id.text1))
                        .setCompoundDrawablesWithIntrinsicBounds(0, inactiveTabsIcon[tabLayout.getSelectedTabPosition()], 0, 0);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ((TextView) tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                        .getCustomView()
                        .findViewById(R.id.text1))
                        .setCompoundDrawablesWithIntrinsicBounds(0, activeTabsIcon[tabLayout.getSelectedTabPosition()], 0, 0);
            }
        });
        tabLayout.getTabAt(0).select();
    }

    private void sendScreenName(int selectedTabPosition) {
        switch (selectedTabPosition) {
            case 0:
                GoogleAnalyticsHelper.trackScreenName(getString(R.string.daily_screen));
                break;
            case 1:
                GoogleAnalyticsHelper.trackScreenName(getString(R.string.cookbook_screen));
                break;
            case 2:
                GoogleAnalyticsHelper.trackScreenName(getString(R.string.buylist_screen));
                break;
            case 3:
                GoogleAnalyticsHelper.trackScreenName(getString(R.string.latest_comments_screen));
                break;
            case 4:
                GoogleAnalyticsHelper.trackScreenName(getString(R.string.user_personal_screen));
                break;


        }


    }

    public void setTabTextIcons() {
        String[] tabsText = {getString(R.string.tab_feed),
                getString(R.string.tab_cookbooks),
                getString(R.string.tab_list),
                getString(R.string.tab_comments),
                getString(R.string.tab_me),
        };
        for (int i = 0; i < tabCount; i++) {
            TabHomeTabLayoutBinding tabBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.tab_home_tab_layout, tabLayout, false);
            View tabView = tabBinding.getRoot();
            tabLayout.getTabAt(i).setCustomView(tabView);
            if (i == 2) {
                tabBinding.txtviewListCount.setVisibility(View.VISIBLE);
            }
            tabBinding.text1.setText(tabsText[i]);
            tabBinding.text1.setCompoundDrawablesWithIntrinsicBounds(0, inactiveTabsIcon[i], 0, 0);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY || requestCode == EditProfileViewModel.REQUEST_CAMERA) {
                if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                    imageUri = data.getData();
                    filePath = CameraUtils.getRealPathFromURI(HomeActivity.this, imageUri);
                    Intent confirmIntent = new Intent(this, ConfirmImageUploadActivity.class);
                    confirmIntent.setData(imageUri);
                    startActivityForResult(confirmIntent, CONFIRM_REQUEST_CODE);
                } else {
                    filePath = CameraUtils.onCaptureImageResult();
                    userProfileFragment.getUserViewModel().uploadImageOnCloudinary(filePath);
                }
                Logger.d("File Path", filePath);
            } else if (requestCode == Constants.CONFIRM_REQUEST_CODE) {
                userProfileFragment.getUserViewModel().uploadImageOnCloudinary(filePath);
            }
        }
        if (requestCode == Constants.USERPROFILE_REQUEST && User.getInstance().isRefreshRequired()) {
            refreshFragment(TAB_USER_PROFILE);
            User.getInstance().setRefreshRequired(false);
        } else if (requestCode == Constants.COOKBOOK_REQUEST) {
            refreshFragment(TAB_COOKBOOKS);
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEventRecieve(AuthUpdateEvent authUpdateEvent) {
        String eventText = authUpdateEvent.getEvent();
        if (eventText.equalsIgnoreCase("updateRequired")) {
            refreshFragment(TAB_USER_PROFILE);
            refreshFragment(TAB_COOKBOOKS);
        } else if (eventText.equalsIgnoreCase("refreshRecipeDetail") || eventText.equals("refreshCookbook")) {
            refreshFragment(TAB_COOKBOOKS);
        } else if (eventText.equalsIgnoreCase("followToggled")) {
            refreshFragment(TAB_USER_PROFILE);
        }
        EventBus.getDefault().removeStickyEvent(authUpdateEvent);
    }

    @Subscribe(sticky = true)
    public void onEventRecieve(CookbookUpdateEvent cookbookUpdateEvent) {
        String eventText = cookbookUpdateEvent.getEvent();
        if (eventText.equalsIgnoreCase("refreshRecipeDetail") || eventText.equals("refreshCookbook")) {
            refreshFragment(TAB_COOKBOOKS);
        }
        EventBus.getDefault().removeStickyEvent(cookbookUpdateEvent);
    }


    @Subscribe(sticky = true)
    public void onUpdateListCounter(ShoppingListCounterEvent counterEvent) {
        TextView listCount = (TextView) tabLayout.getTabAt(2).getCustomView().findViewById(R.id.txtview_list_count);
        listCount.setText(String.valueOf(counterEvent.getCount()));
        EventBus.getDefault().removeStickyEvent(counterEvent);
    }

    @Subscribe(sticky = true)
    public void onUpdateShoppingList(AddToListEvent addToListEvent) {
        refreshFragment(TAB_SHOPPING_LIST);
        EventBus.getDefault().removeStickyEvent(addToListEvent);
    }

    private void refreshFragment(int postionFragmentToRefresh) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = mFragList.get(postionFragmentToRefresh);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        showHomeTabOnBackPressed();
    }

    private void showHomeTabOnBackPressed() {
        int selectedTab = tabLayout.getSelectedTabPosition();
        if (selectedTab != 0) {
            tabLayout.setScrollPosition(0, 0, true);
            viewPager.setCurrentItem(0);

        } else {
            super.onBackPressed();

        }
    }


    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final Bundle bundle = intent.getExtras();
            Intent dialogIntent = new Intent(HomeActivity.this, NotificationDialogActivity.class);
            dialogIntent.putExtras(bundle);
            startActivity(dialogIntent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TextView listCount = (TextView) tabLayout.getTabAt(2).getCustomView().findViewById(R.id.txtview_list_count);
        int margin = getResources().getDimensionPixelSize(R.dimen.home_tab_margin_end);
        RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) listCount.getLayoutParams();
        llp.setMarginEnd(margin);
        listCount.setLayoutParams(llp);
    }

    private List<Fragment> mFragList = new ArrayList<>();

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mFragList.size() == 5) {
            if (fragment instanceof UserFeedFragment) {
                mFragList.set(0, fragment);
            } else if (fragment instanceof MyCookbooksFragment) {
                mFragList.set(1, fragment);
            } else if (fragment instanceof ShoppingListFragment) {
                mFragList.set(2, fragment);
            } else if (fragment instanceof CommentsFragment) {
                mFragList.set(3, fragment);
            } else if (fragment instanceof UserProfileFragment) {
                mFragList.set(4, fragment);
            }
        } else {
            mFragList.add(fragment);
        }
    }
}