package com.kokaihop.authentication.login;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityLoginBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
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
        GoogleAnalyticsHelper.trackScreenName( getString(R.string.login_screen));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginViewModel.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        if (FacebookAuthentication.callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
