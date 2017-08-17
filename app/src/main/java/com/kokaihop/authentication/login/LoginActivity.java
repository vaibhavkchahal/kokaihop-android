package com.kokaihop.authentication.login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityLoginBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.FacebookAuthentication;

import static com.kokaihop.utility.FacebookAuthentication.callbackManager;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        loginBinding.setViewModel(loginViewModel);
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.login_screen));

    }

    @Override
    protected void onResume() {
        super.onResume();
        clearEditFields();
    }

    private void clearEditFields() {
        loginViewModel.setUserName("");
        loginViewModel.setPassword("");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loginBinding.scrollviewLogin.setPadding(0, (int) getResources().getDimension(R.dimen.padding_top_login_scroll), 0, 0);
        setMarginToViewOnRotation(loginBinding.textviewOrLogin, (int) getResources().getDimension(R.dimen.margin_top_login_or_text));
        setMarginToViewOnRotation(loginBinding.editTextEmail, (int) getResources().getDimension(R.dimen.margin_top_login_email));
        setMarginToViewOnRotation(loginBinding.editTextPassword, (int) getResources().getDimension(R.dimen.margin_top_login_passwd));
        setMarginToViewOnRotation(loginBinding.buttonLogin, (int) getResources().getDimension(R.dimen.margin_top_login_button));
        setMarginToViewOnRotation(loginBinding.textviewForgotPassword, (int) getResources().getDimension(R.dimen.margin_top_forgot_passwd));
        setMarginToViewOnRotation(loginBinding.textViewSignUpNow, (int) getResources().getDimension(R.dimen.margin_top_signupnow));
    }

    private void setMarginToViewOnRotation(View view, int marginTop) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, marginTop, 0, 0);
        view.setLayoutParams(params);
    }
}
