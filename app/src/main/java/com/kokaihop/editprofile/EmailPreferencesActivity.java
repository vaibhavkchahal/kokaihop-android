package com.kokaihop.editprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEmailPreferencesBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;

public class EmailPreferencesActivity extends AppCompatActivity {

    EmailPreferencesViewModel preferencesViewModel;
    ActivityEmailPreferencesBinding preferencesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesBinding = DataBindingUtil.setContentView(this, R.layout.activity_email_preferences);
        preferencesViewModel = new EmailPreferencesViewModel(this);
        preferencesBinding.setViewModel(preferencesViewModel);
        GoogleAnalyticsHelper.trackScreenName(this, getString(R.string.email_prefrence_screen));


    }
}
