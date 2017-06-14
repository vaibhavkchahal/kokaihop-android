package com.kokaihop.home;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityHomeBinding;
import com.altaworks.kokaihop.ui.databinding.TabHomeTabLayoutBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.customviews.NonSwipeableViewPager;
import com.kokaihop.editprofile.EditProfileViewModel;
import com.kokaihop.feed.PagerTabAdapter;
import com.kokaihop.utility.CameraUtils;

import static com.kokaihop.KokaihopApplication.getContext;
import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;

public class HomeActivity extends BaseActivity {
    private NonSwipeableViewPager viewPager;
    private TabLayout tabLayout;
    private ActivityHomeBinding activityHomeBinding;
    private int tabCount = 5;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setTabView();
    }

    @Override
    public void onResume() {
        super.onResume();


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
        adapter.addFrag(new UserFeedFragment(), getString(R.string.tab_feed));
        adapter.addFrag(new CookbooksFragment(), getString(R.string.tab_cookbooks));
        adapter.addFrag(new ListFragment(), getString(R.string.tab_list));
        adapter.addFrag(new CommentsFragment(), getString(R.string.tab_comments));
        adapter.addFrag(new UserProfileFragment(), getString(R.string.tab_me));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        setTabTextIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                        .getCustomView()
                        .findViewById(R.id.text1))
                        .setCompoundDrawablesWithIntrinsicBounds(0, activeTabsIcon[tabLayout.getSelectedTabPosition()], 0, 0);
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
            tabBinding.text1.setText(tabsText[i]);
            tabBinding.text1.setCompoundDrawablesWithIntrinsicBounds(0, inactiveTabsIcon[i], 0, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                data.getData();
            } else if (requestCode == EditProfileViewModel.REQUEST_CAMERA) {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.userChoosenTask.equals(getString(R.string.take_photo)))
                        CameraUtils.cameraIntent(getContext());
                    else if (CameraUtils.userChoosenTask.equals(getString(R.string.choose_from_library)))
                        CameraUtils.galleryIntent(getContext());
                } else {
                }
                break;
        }
    }
}
