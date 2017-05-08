package com.kokaihop.login;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.BaseViewModel;
import com.kokaihop.utility.Constants;

import java.util.Arrays;


public class LoginViewModel extends BaseViewModel {

    private String userName = "rajendra.singh@tothenew.com";
    private String password = "kokaihop";

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
        new LoginApiHelper(view.getContext()).doLogin(userName, password, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
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

    // request forgot password.
    public void forgot(final View view) {
        if (userName.isEmpty() || !AppUtility.isValidEmail(userName)) {
            Toast.makeText(view.getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        setProgressVisible(true);
        new LoginApiHelper(view.getContext()).doForgot(userName, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ForgotApiResponse forgotApiResponse = (ForgotApiResponse) response;
                if (forgotApiResponse.isSuccess()) {
                    setProgressVisible(false);
                    Toast.makeText(view.getContext(), R.string.forgot_success_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // facebook login integration.
    public void facebookLogin(final View view) {
        LoginActivity activity = (LoginActivity) view.getContext();
        CallbackManager callbackManager = activity.getCallbackManager();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(activity.getResources().getString(R.string.facebook_email_permisson), activity.getResources().getString(R.string.facebook_public_profile_permisson)));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        Log.i("login acces token--->", "" + accessToken.getToken());
                        Toast.makeText(view.getContext(), R.string.sucess_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // action on cancel
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(view.getContext(), R.string.failed_login, Toast.LENGTH_SHORT).show();
                    }
                });
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
