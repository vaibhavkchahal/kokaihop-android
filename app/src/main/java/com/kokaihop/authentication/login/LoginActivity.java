package com.kokaihop.authentication.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityLoginBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.FacebookAuthentication;

import static com.kokaihop.utility.FacebookAuthentication.callbackManager;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        loginBinding.setViewModel(loginViewModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookAuthentication.callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
