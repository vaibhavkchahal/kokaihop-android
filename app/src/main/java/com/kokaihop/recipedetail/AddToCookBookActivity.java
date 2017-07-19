package com.kokaihop.recipedetail;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityCookbookBinding;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbookLoginBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.cookbooks.MyCookbooksViewModel;
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
        String accessToken = SharedPrefUtils.getSharedPrefStringData(this, Constants.ACCESS_TOKEN);
        if ((accessToken == null) || accessToken.isEmpty()) {
            FragmentCookbookLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.fragment_cookbook_login);
            MyCookbooksViewModel viewModel = new MyCookbooksViewModel(null, this, null, null);
            binding.setViewModel(viewModel);
        } else {
            ActivityCookbookBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cookbook);
            setupArguments();
            AddToCookbookFragment addToCookbookFragment = new AddToCookbookFragment();
            binding.setViewModel(new AddToCookbookViewModel(addToCookbookFragment, this));
            addToCookbookFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(binding.rlAddToCookbook.getId(), addToCookbookFragment).commit();
        }
    }

    private void setupArguments() {
        bundle = new Bundle();
        bundle.putString(Constants.USER_ID, SharedPrefUtils.getSharedPrefStringData(this, Constants.USER_ID));
        bundle.putString(Constants.FRIENDLY_URL, SharedPrefUtils.getSharedPrefStringData(this, Constants.FRIENDLY_URL));
        bundle.putString(Constants.COLLECTION_MAPPING, getIntent().getStringExtra(Constants.COLLECTION_MAPPING));
        bundle.putString(Constants.RECIPE_ID, getIntent().getStringExtra(Constants.RECIPE_ID));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == Activity.RESULT_OK) {
            this.recreate();
        }
    }
}
