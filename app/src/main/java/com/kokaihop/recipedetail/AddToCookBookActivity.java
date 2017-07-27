package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityCookbookBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Vaibhav Chahal on 12/6/17.
 */

public class AddToCookBookActivity extends BaseActivity {

    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCookbookBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cookbook);
        setupArguments();
        AddToCookbookFragment addToCookbookFragment = new AddToCookbookFragment();
        binding.setViewModel(new AddToCookbookViewModel(addToCookbookFragment, this));
        addToCookbookFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(binding.rlAddToCookbook.getId(), addToCookbookFragment).commit();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.cookbook_picker_screen));

    }

    private void setupArguments() {
        bundle = new Bundle();
        bundle.putString(Constants.USER_ID, SharedPrefUtils.getSharedPrefStringData(this, Constants.USER_ID));
        bundle.putString(Constants.FRIENDLY_URL, SharedPrefUtils.getSharedPrefStringData(this, Constants.FRIENDLY_URL));
        bundle.putString(Constants.COLLECTION_MAPPING, getIntent().getStringExtra(Constants.COLLECTION_MAPPING));
        bundle.putString(Constants.RECIPE_ID, getIntent().getStringExtra(Constants.RECIPE_ID));
    }
}
