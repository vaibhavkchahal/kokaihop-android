package com.kokaihop.editprofile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityChangePasswordBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordActivity extends BaseActivity{

    ActivityChangePasswordBinding passwordBinding;
    ChangePasswordViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        viewModel = new ChangePasswordViewModel(this,passwordBinding);
        passwordBinding.setViewModel(viewModel);
        GoogleAnalyticsHelper.trackScreenName(this, getString(R.string.password_edit_screen));

    }
}
