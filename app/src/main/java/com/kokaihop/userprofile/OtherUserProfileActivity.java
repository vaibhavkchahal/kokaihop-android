package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityOtherUserProfileBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;

public class OtherUserProfileActivity extends BaseActivity {

    private ActivityOtherUserProfileBinding binding;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user_profile);
        String userId = getIntent().getStringExtra(Constants.USER_ID);
        String friendlyUrl = getIntent().getStringExtra(Constants.FRIENDLY_URL);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        bundle.putString(Constants.FRIENDLY_URL, friendlyUrl);
        fragment = new OtherUserProfileFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(binding.clOtherUserContainer.getId(), fragment).commit();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.user_public_screen));

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
