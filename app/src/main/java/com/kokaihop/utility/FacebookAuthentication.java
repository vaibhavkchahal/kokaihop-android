package com.kokaihop.utility;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kokaihop.authentication.FacebookAuthRequest;
import com.kokaihop.authentication.FacebookUserLocation;
import com.kokaihop.authentication.FacebookUserName;
import com.kokaihop.base.BaseActivity;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class FacebookAuthentication {

    public static CallbackManager callbackManager = CallbackManager.Factory.create();

    public void facebookLogin(final View view, final FacebookResponseCallback facebookResponseCallback) {
        BaseActivity activity = (BaseActivity) view.getContext();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(activity.getResources().getString(R.string.facebook_email_permisson), activity.getResources().getString(R.string.facebook_public_profile_permisson), "user_location", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                FacebookAuthRequest facebookAuthRequest = getFacebookAuthRequestObject(user, accessToken);
                                if (facebookAuthRequest != null) {
                                    facebookResponseCallback.onSuccess(facebookAuthRequest);
                                } else {
                                    facebookResponseCallback.onfailure(view.getContext().getString(R.string.unable_access_fb_detai));
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,email,name,link,first_name,last_name,location");
                        request.setParameters(parameters);
                        request.executeAsync();
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

    @NonNull
    private FacebookAuthRequest getFacebookAuthRequestObject(JSONObject user, AccessToken accessToken) {
        String email = user.optString("email");
        String fullName = user.optString("name");
        String firstName = user.optString("first_name");
        String lastName = user.optString("last_name");
        String facebookId = user.optString("id");
        JSONObject location = user.optJSONObject("location");
        String locationId = location.optString("id");
        String locationName = location.optString("name");
        FacebookUserName facebookName = new FacebookUserName(firstName, fullName, lastName);
        FacebookUserLocation facebookLocation = new FacebookUserLocation(locationId, locationName);
        return new FacebookAuthRequest(facebookName, accessToken.getToken(), email, facebookId, facebookLocation);
    }

    public interface FacebookResponseCallback {
        void onSuccess(FacebookAuthRequest response);

        void onfailure(String error);
    }

}
