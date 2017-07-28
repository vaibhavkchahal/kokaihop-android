package com.kokaihop.editprofile;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySettingsBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    SettingsViewModel viewModel;

    public SettingsActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = new SettingsViewModel(this);
        settingsBinding.setViewModel(viewModel);
        settingsBinding.settingsLogout.setOnClickListener(this);
        settingsBinding.settingsChangePassword.setOnClickListener(this);
        settingsBinding.settingsEmailPreferences.setOnClickListener(this);
        settingsBinding.settingsEditProfile.setOnClickListener(this);
        settingsBinding.settingsIvBack.setOnClickListener(this);
        GoogleAnalyticsHelper.trackScreenName( getString(R.string.settings_screen));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_logout:
                viewModel.logout();
                break;
            case R.id.settings_iv_back:
                finish();
                break;

            case R.id.settings_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.settings_email_preferences:
                startActivity(new Intent(this, EmailPreferencesActivity.class));
                break;
            case R.id.settings_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }
}
