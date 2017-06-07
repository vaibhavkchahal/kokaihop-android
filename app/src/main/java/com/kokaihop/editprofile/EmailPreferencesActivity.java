package com.kokaihop.editprofile;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEmailPreferencesBinding;

public class EmailPreferencesActivity extends AppCompatActivity {

    EmailPreferencesViewModel preferencesViewModel;
    ActivityEmailPreferencesBinding preferencesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesBinding =  DataBindingUtil.setContentView(this,R.layout.activity_email_preferences);
        preferencesViewModel = new EmailPreferencesViewModel(this,preferencesBinding);
        preferencesBinding.setViewModel(preferencesViewModel);


    }
}
