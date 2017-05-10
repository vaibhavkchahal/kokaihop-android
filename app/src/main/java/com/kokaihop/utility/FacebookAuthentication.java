package com.kokaihop.utility;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kokaihop.base.BaseActivity;

import java.util.Arrays;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class FacebookAuthentication {

    public static CallbackManager callbackManager = CallbackManager.Factory.create();

    public void facebookLogin(final View view) {
        BaseActivity activity = (BaseActivity) view.getContext();
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

}
