package com.kokaihop.home.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityUserSettingsBinding;
import com.kokaihop.home.HomeActivity;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

public class UserSettingsActivity extends AppCompatActivity {

    private Context context;
    private ActivityUserSettingsBinding userSettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        userSettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_settings);

        userSettingsBinding.settingsLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, null);
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
}
