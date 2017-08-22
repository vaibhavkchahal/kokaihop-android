package com.kokaihop.authentication.signup;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySignUpBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.city.CityDetails;
import com.kokaihop.city.CityLocation;
import com.kokaihop.utility.FacebookAuthentication;

public class SignUpActivity extends BaseActivity {

    private SignUpViewModel signUpViewModel;
    private ActivitySignUpBinding signUpBinding;
    private boolean clearSignupFields = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel();
        signUpBinding.setViewModel(signUpViewModel);
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.register_screen));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clearSignupFields) {
            clearEditFields();
        }
    }

    private void clearEditFields() {
        signUpViewModel.setName("");
        signUpViewModel.setPassword("");
        signUpViewModel.setCity("");
        signUpViewModel.setUserName("");
        signUpViewModel.setNewsletter(0);
        signUpViewModel.setSuggestion(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignUpViewModel.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CityDetails citySelected = data.getParcelableExtra("citySelected");
            CityLocation cityLocation = new CityLocation(citySelected);
            signUpViewModel.setCityLocation(cityLocation);
            signUpViewModel.setCity(citySelected.getName());
            clearSignupFields = false;
        } else {
            if (FacebookAuthentication.callbackManager != null) {
                FacebookAuthentication.callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setMarginToViewOnRotation(signUpBinding.editTextEmail, (int) getResources().getDimension(R.dimen.signup_et_email_margin_top));
        setMarginToViewOnRotation(signUpBinding.editTextCity, (int) getResources().getDimension(R.dimen.signup_et_margin_top));
        setMarginToViewOnRotation(signUpBinding.editTextPassword, (int) getResources().getDimension(R.dimen.signup_et_margin_top));
        setMarginToViewOnRotation(signUpBinding.scrollviewSignup, (int) getResources().getDimension(R.dimen.signup_sv_formcontainer_margin_top));
        setMarginToViewOnRotation(signUpBinding.buttonFacebookSignUp, (int) getResources().getDimension(R.dimen.signup_btn_facebook_margin_top));
        setMarginToViewOnRotation(signUpBinding.buttonSignUp, (int) getResources().getDimension(R.dimen.signup_btn_signup_margin_top));
        setMarginToViewOnRotation(signUpBinding.textViewAlreadySignUp, (int) getResources().getDimension(R.dimen.signup_tv_login_margin_top));
        setMarginToViewOnRotation(signUpBinding.checkBoxDagensRecept, (int) getResources().getDimension(R.dimen.signup_cb_margin_top));
        setMarginToViewOnRotation(signUpBinding.checkBoxNyhetsbrev, (int) getResources().getDimension(R.dimen.signup_cb_margin_top));
    }

    private void setMarginToViewOnRotation(View view, int marginTop) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, marginTop, 0, 0);
        view.setLayoutParams(params);
    }
}
