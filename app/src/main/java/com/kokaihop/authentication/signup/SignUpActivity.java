package com.kokaihop.authentication.signup;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySignUpBinding;
import com.facebook.CallbackManager;
import com.kokaihop.city.CityDetails;
import com.kokaihop.city.CityLocation;
import com.kokaihop.utility.BaseActivity;

public class SignUpActivity extends BaseActivity {

    private SignUpViewModel signUpViewModel;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
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
            Log.e("City Selected", citySelected.getName());
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
