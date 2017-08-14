package com.kokaihop.authentication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.authentication.AuthenticationApiResponse;
import com.kokaihop.authentication.FacebookAuthRequest;
import com.kokaihop.authentication.forgotpassword.ForgotPasswordActivity;
import com.kokaihop.authentication.signup.SignUpActivity;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.AuthUpdateEvent;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.ValidationUtils;

import org.greenrobot.eventbus.EventBus;

import static com.kokaihop.utility.AppUtility.showHomeScreen;
import static com.kokaihop.utility.Constants.EXTRA_FROM;


public class LoginViewModel extends BaseViewModel {

    private String userName = "";
    private String password = "";
    public static final int REQUEST_CODE = 10;

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
                Activity activity = (Activity) context;
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.user_category), context.getString(R.string.user_login_action), context.getString(R.string.user_native_login_label), 1);

                setProgressVisible(false);
                SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                SharedPrefUtils.setSharedPrefStringData(context, Constants.USER_ID, response.getUserAuthenticationDetail().getId());
                SharedPrefUtils.setSharedPrefStringData(context, Constants.FRIENDLY_URL, response.getUserAuthenticationDetail().getFriendlyUrl());
                String from = ((LoginActivity) context).getIntent().getStringExtra(EXTRA_FROM);
                Toast.makeText(context, R.string.sucess_login, Toast.LENGTH_LONG).show();
                if (from != null && from.equals("loginRequired")) {
                    EventBus.getDefault().postSticky(new AuthUpdateEvent("updateRequired"));
                    ((LoginActivity) context).finish();
                } else {
                    showHomeScreen(context);
                }

            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Activity activity = (Activity) context;
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.user_category), context.getString(R.string.user_login_action), context.getString(R.string.user_native_login_label), 0);


            }
        });

    }


    public void openForgotPasswordScreen(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), ForgotPasswordActivity.class));
    }

    public void openSignupScreen(View view) {
        Intent intent = new Intent(view.getContext(), SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Activity activity = (Activity) view.getContext();
//        boolean isComingFromLike = ((LoginActivity) view.getContext()).getIntent().getBooleanExtra("isComingFromLike", false);
        String from = ((LoginActivity) view.getContext()).getIntent().getStringExtra(EXTRA_FROM);
        if (from != null) {
            intent.putExtra(EXTRA_FROM, from);
            activity.startActivityForResult(intent, REQUEST_CODE);
        } else {
            activity.startActivity(intent);
        }

    }

    public void facebookLogin(final View view) {
        FacebookAuthentication authentication = new FacebookAuthentication();
        final Activity activity = (Activity) view.getContext();
        authentication.facebookLogin(view, new FacebookAuthentication.FacebookResponseCallback() {
            @Override
            public void onSuccess(FacebookAuthRequest facebookAuthRequest) {
                setProgressVisible(true);
                new AuthenticationApiHelper(view.getContext()).facebookloginSignup(facebookAuthRequest, new IApiRequestComplete<AuthenticationApiResponse>() {
                    @Override
                    public void onSuccess(AuthenticationApiResponse response) {

                        GoogleAnalyticsHelper.trackEventAction(view.getContext().getString(R.string.user_category), view.getContext().getString(R.string.user_login_action), view.getContext().getString(R.string.user_facebook_login_label), 1);

                        setProgressVisible(false);
                        Context context = view.getContext();
                        SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                        Toast.makeText(context, R.string.sucess_login, Toast.LENGTH_LONG).show();
                        if (response.getUserAuthenticationDetail() != null) {
                            SharedPrefUtils.setSharedPrefStringData(context, Constants.USER_ID, response.getUserAuthenticationDetail().getId());
                            SharedPrefUtils.setSharedPrefStringData(context, Constants.FRIENDLY_URL, response.getUserAuthenticationDetail().getFriendlyUrl());
                            SharedPrefUtils.setSharedPrefStringData(context, Constants.LOGIN_TYPE, Constants.FACEBOOK_LOGIN);
                        }
//                        boolean isComingFromLike = ((LoginActivity) view.getContext()).getIntent().getBooleanExtra("isComingFromLike", false);
                        String from = ((LoginActivity) view.getContext()).getIntent().getStringExtra(EXTRA_FROM);
                        if (from != null && from.equals("loginRequired")) {
                            EventBus.getDefault().postSticky(new AuthUpdateEvent("updateRequired"));
                            ((LoginActivity) view.getContext()).finish();

                        } else {
                            AppUtility.showHomeScreen(view.getContext());
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                        GoogleAnalyticsHelper.trackEventAction(view.getContext().getString(R.string.user_category), view.getContext().getString(R.string.user_login_action), view.getContext().getString(R.string.user_facebook_login_label), 0);

                    }

                });

            }

            @Override
            public void onfailure(String error) {
                Toast.makeText(view.getContext(), error, Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
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
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void destroy() {
    }

}
