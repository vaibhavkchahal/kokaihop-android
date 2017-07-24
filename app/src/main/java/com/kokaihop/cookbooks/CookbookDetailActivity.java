package com.kokaihop.cookbooks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityCookbookDetailBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.utility.Constants;

public class CookbookDetailActivity extends AppCompatActivity {

    ActivityCookbookDetailBinding binding;
    CookbookDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cookbook_detail);
        String userFriendlyUrl = getIntent().getStringExtra(Constants.USER_FRIENDLY_URL);
        String cookbookFriendlyUrl = getIntent().getStringExtra(Constants.COOKBOOK_FRIENDLY_URL);
        String cookbookTitle = getIntent().getStringExtra(Constants.COOKBOOK_TITLE);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_FRIENDLY_URL, userFriendlyUrl);
        bundle.putString(Constants.COOKBOOK_FRIENDLY_URL, cookbookFriendlyUrl);
        bundle.putString(Constants.COOKBOOK_TITLE, cookbookTitle);
        fragment = new CookbookDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(binding.llCookbookDetail.getId(), fragment).commit();
        GoogleAnalyticsHelper.trackScreenName(this, getString(R.string.cookbook_specific_screen));

    }
}
