package com.kokaihop.authentication.signup;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.city.CityDetails;
import com.kokaihop.city.CityLocation;
import com.kokaihop.utility.FacebookAuthentication;

public class SignUpActivity extends BaseActivity {

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.altaworks.kokaihop.ui.databinding.ActivitySignUpBinding signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel();
        signUpBinding.setViewModel(signUpViewModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignUpViewModel.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CityDetails citySelected = data.getParcelableExtra("citySelected");
            CityLocation cityLocation = new CityLocation(citySelected);
            signUpViewModel.setCityLocation(cityLocation);
            signUpViewModel.setCity(citySelected.getName());
        } else {
            if (FacebookAuthentication.callbackManager != null) {
                FacebookAuthentication.callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
