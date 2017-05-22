package com.kokaihop.authentication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.authentication.AuthenticationApiResponse;
import com.kokaihop.authentication.FacebookAuthRequest;
import com.kokaihop.authentication.forgotpassword.ForgotPasswordActivity;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.ValidationUtils;

import static com.kokaihop.utility.AppUtility.showHomeScreen;


public class LoginViewModel extends BaseViewModel {

    private String userName = "";
    private String password = "";
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
    public void login(View view) {
        final Context context = view.getContext();
        if (loginValidations(context, userName, password))
            return;
        setProgressVisible(true);
        new AuthenticationApiHelper(view.getContext()).doLogin(userName, password, new IApiRequestComplete<AuthenticationApiResponse>() {
            @Override
            public void onSuccess(AuthenticationApiResponse response) {
                setProgressVisible(false);
                SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                SharedPrefUtils.setSharedPrefStringData(context, Constants.USER_ID, response.getUser().getId());
                showHomeScreen(context);
                Toast.makeText(context, R.string.sucess_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(AuthenticationApiResponse response) {
                setProgressVisible(false);
            }
        });

    }



    public void openForgotPasswordScreen(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), ForgotPasswordActivity.class));
    }

    public void openSignupScreen(View view) {
        Activity activity = (Activity) view.getContext();
//        activity.startActivity(new Intent(view.getContext(), SignUpActivity.class));
        activity.finish();
    }

    public void facebookLogin(final View view) {
        FacebookAuthentication authentication = new FacebookAuthentication();
        authentication.facebookLogin(view, new FacebookAuthentication.FacebookResponseCallback() {
            @Override
            public void onSuccess(FacebookAuthRequest facebookAuthRequest) {
                setProgressVisible(true);
                new AuthenticationApiHelper(view.getContext()).facebookloginSignup(facebookAuthRequest, new IApiRequestComplete<AuthenticationApiResponse>() {
                    @Override
                    public void onSuccess(AuthenticationApiResponse response) {
                        setProgressVisible(false);
                        Toast.makeText(view.getContext(), R.string.sucess_login, Toast.LENGTH_SHORT).show();
                        SharedPrefUtils.setSharedPrefStringData(view.getContext(), Constants.ACCESS_TOKEN, response.getToken());
                        AppUtility.showHomeScreen(view.getContext());
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(AuthenticationApiResponse response) {
                        setProgressVisible(false);
                        String message = response.getErrorEmail().getDetail().getMessage();
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onfailure(String error) {
                Toast.makeText(view.getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finishLogin(final View view) {
        ((Activity) view.getContext()).finish();
    }

    // validate fields for login.
    private boolean loginValidations(Context context, String username, String password) {
        if (username.isEmpty() || !ValidationUtils.isValidEmail(username)) {
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MAX_LENGTH) {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void destroy() {

    }
}
