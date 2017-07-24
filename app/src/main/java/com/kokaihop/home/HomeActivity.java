package com.kokaihop.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityHomeBinding;
import com.altaworks.kokaihop.ui.databinding.TabHomeTabLayoutBinding;
import com.google.android.gms.ads.MobileAds;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.customviews.NonSwipeableViewPager;
import com.kokaihop.customviews.NotificationDialogActivity;
import com.kokaihop.editprofile.EditProfileViewModel;
import com.kokaihop.feed.PagerTabAdapter;
import com.kokaihop.userprofile.ConfirmImageUploadActivity;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;
import static com.kokaihop.utility.Constants.CONFIRM_REQUEST_CODE;

public class HomeActivity extends BaseActivity {
    private NonSwipeableViewPager viewPager;
    private TabLayout tabLayout;
    private ActivityHomeBinding activityHomeBinding;
    private int tabCount = 5;
    HomeViewModel viewModel;
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
                ((TextView) tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                        .getCustomView()
                        .findViewById(R.id.text1))
                        .setCompoundDrawablesWithIntrinsicBounds(0, activeTabsIcon[tabLayout.getSelectedTabPosition()], 0, 0);
                if (tabLayout.getSelectedTabPosition() == 1) {
                    refreshFragment(1);
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
                    userProfileFragment.userViewModel.uploadImageOnCloudinary(filePath);
                }
                Logger.d("File Path", filePath);
            } else if (requestCode == Constants.CONFIRM_REQUEST_CODE) {
                userProfileFragment.userViewModel.uploadImageOnCloudinary(filePath);
            }
        }
        if (requestCode == Constants.USERPROFILE_REQUEST && User.getInstance().isRefreshRequired()) {
            refreshFragment(4);
            User.getInstance().setRefreshRequired(false);
        } else if (requestCode == Constants.COOKBOOK_REQUEST) {
            refreshFragment(1);
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
            refreshFragment(4);
            refreshFragment(1);
        } else if (eventText.equalsIgnoreCase("refreshRecipeDetail") || eventText.equals("refreshCookbook")) {
            refreshFragment(1);
        } else if (eventText.equalsIgnoreCase("followToggled")) {
            refreshFragment(4);
        }
        EventBus.getDefault().removeStickyEvent(authUpdateEvent);
    }


    @Subscribe(sticky = true)
    public void onUpdateListCounter(ShoppingListCounterEvent counterEvent) {
        TextView listCount = (TextView) tabLayout.getTabAt(2).getCustomView().findViewById(R.id.txtview_list_count);
        listCount.setText(String.valueOf(counterEvent.getCount()));
        EventBus.getDefault().removeStickyEvent(counterEvent);
    }

    private void refreshFragment(int postionFragmentToRefresh) {
        PagerTabAdapter pagerTabAdapter = (PagerTabAdapter) viewPager.getAdapter();
        Fragment fragment = pagerTabAdapter.getItem(postionFragmentToRefresh);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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

           /* AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
            dialog.setTitle(getString(R.string.app_name));
            dialog.setMessage(bundle.getString("message"));
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HomeActivity.this, RecipeDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();*/
        }
    }
}
