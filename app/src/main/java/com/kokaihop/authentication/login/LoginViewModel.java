package com.kokaihop.authentication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.authentication.forgotpassword.ForgotPasswordActivity;
import com.kokaihop.authentication.signup.SignUpActivity;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.BaseViewModel;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;


public class LoginViewModel extends BaseViewModel {

    private String userName = "rajendra.singh@tothenew.com";
    private String password = "kokaihop";
    private static final int REQUEST_CODE = 10;

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.notifyPropertyChanged(BR.password);
    }

    // request login.
    public void login(final View view) {
        if (loginValidations(view, userName, password))
            return;
        setProgressVisible(true);
        new AuthenticationApiHelper(view.getContext()).doLogin(userName, password, new IApiRequestComplete<LoginApiResponse>() {
            @Override
            public void onSuccess(LoginApiResponse response) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), R.string.sucess_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openForgotPasswordScreen(View view) {
        Context context = view.getContext();
        view.getContext().startActivity(new Intent(context, ForgotPasswordActivity.class));
    }

    public void openSignupScreen(View view) {
        Activity activity = (Activity) view.getContext();
        activity.startActivity(new Intent(activity, SignUpActivity.class));
    }

    public void facebookLogin(final View view) {
        FacebookAuthentication authentication = new FacebookAuthentication();
        authentication.facebookLogin(view);
    }

    // validate fields for login.
    private boolean loginValidations(View view, String username, String password) {
        if (username.isEmpty() || !AppUtility.isValidEmail(username)) {
            Toast.makeText(view.getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MAX_LENGTH) {
            Toast.makeText(view.getContext(), R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
