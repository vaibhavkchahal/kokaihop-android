package com.kokaihop.authentication.signup;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySignUpBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.city.CityDetails;
import com.kokaihop.city.CityLocation;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;

public class SignUpActivity extends BaseActivity {

    private SignUpViewModel signUpViewModel;
    private ActivitySignUpBinding signUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel();
        checkSavedData(savedInstanceState);
        signUpBinding.setViewModel(signUpViewModel);
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.register_screen));

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveDataOnRotation(outState);
    }

    private void saveDataOnRotation(Bundle outState) {
        outState.putString(Constants.NAME, signUpBinding.name.getText().toString());
        outState.putString(Constants.EMAIL, signUpBinding.email.getText().toString());
        outState.putString(Constants.PASSWORD, signUpBinding.password.getText().toString());
        outState.putString(Constants.CITY, signUpBinding.city.getText().toString());
        if (signUpBinding.checkBoxDagensRecept.isChecked()) {
            outState.putInt(Constants.SIGNUP_FIRST_CHECKBOX, 1);
        } else {
            outState.putInt(Constants.SIGNUP_FIRST_CHECKBOX, 0);
        }
        if (signUpBinding.checkBoxNyhetsbrev.isChecked()) {
            outState.putInt(Constants.SIGNUP_SECOND_CHECKBOX, 1);
        } else {
            outState.putInt(Constants.SIGNUP_SECOND_CHECKBOX, 0);
        }
    }

    private void checkSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            signUpViewModel.setName(savedInstanceState.getString(Constants.NAME));
            signUpViewModel.setUserName(savedInstanceState.getString(Constants.EMAIL));
            signUpViewModel.setPassword(savedInstanceState.getString(Constants.PASSWORD));
            signUpViewModel.setCity(savedInstanceState.getString(Constants.CITY));
            signUpViewModel.setNewsletter(savedInstanceState.getInt(Constants.SIGNUP_FIRST_CHECKBOX));
            signUpViewModel.setSuggestion(savedInstanceState.getInt(Constants.SIGNUP_SECOND_CHECKBOX));
        }
    }
}
