package com.kokaihop.authentication.forgotpassword;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityForgotPasswordBinding;
import com.kokaihop.utility.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgotPasswordBinding  binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        binding.setViewModel(new ForgotPaswdViewModel());
    }
}
