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

    public void login(final View view) {
        String username = getUserName();
        String password = getPassword();
        if (username.isEmpty() || !AppUtility.isValidEmail(username)) {
            Toast.makeText(view.getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || password.length() < 7) {
            Toast.makeText(view.getContext(), R.string.password_validation_message, Toast.LENGTH_SHORT).show();
            return;
        }
        setProgressVisible(true);
        new LoginApiHelper(view.getContext()).doLogin(username, password, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void forgot(final View view) {
        String username = getUserName();
        setProgressVisible(true);
        new LoginApiHelper(view.getContext()).doForgot(username, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "failure!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void facebookLogin(final View view) {
        LoginActivity activity = (LoginActivity) view.getContext();
        CallbackManager callbackManager = activity.getCallbackManager();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        Log.i("login acces token--->", "" + accessToken.getToken());
                        Toast.makeText(view.getContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i("login failed", "login failed");
                        Toast.makeText(view.getContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
