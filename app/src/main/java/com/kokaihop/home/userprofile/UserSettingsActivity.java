package com.kokaihop.home.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityUserSettingsBinding;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityUserSettingsBinding userSettingsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettingsBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_settings);
        userSettingsBinding.settingsIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
