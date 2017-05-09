package com.kokaihop.authentication;

import android.content.Context;

import com.kokaihop.authentication.forgotpassword.ForgotApiResponse;
import com.kokaihop.authentication.login.LoginApiResponse;
import com.kokaihop.authentication.signup.SignUpSettings;
import com.kokaihop.city.SignUpCityLocation;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.ResponseHandler;

import retrofit2.Call;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class AuthenticationApiHelper {
    private Context context;
    private AuthenticationApi authenticationApi;

    public AuthenticationApiHelper(Context context) {
        this.context = context;
        this.authenticationApi = RetrofitClient.getInstance().create(AuthenticationApi.class);
    }

    public void doLogin(String email, String password, final IApiRequestComplete successInterface) {
        Call<LoginApiResponse> loginApiResponseCall = authenticationApi.doLogin(email, password);
        loginApiResponseCall.enqueue(new ResponseHandler<LoginApiResponse>(successInterface));
    }


    public void doForgot(String email, final IApiRequestComplete successInterface) {
        Call<ForgotApiResponse> forgotApiResponseCall = authenticationApi.forgot(email);
        forgotApiResponseCall.enqueue(new ResponseHandler<ForgotApiResponse>(successInterface));
    }

    public void signup(String name, String email, String password, SignUpCityLocation cityLocation, SignUpSettings signUpSettings, final IApiRequestComplete successInterface) {
        Call<ForgotApiResponse> forgotApiResponseCall = authenticationApi.signUp(email,cityLocation,name,signUpSettings,password);
        forgotApiResponseCall.enqueue(new ResponseHandler<ForgotApiResponse>(successInterface));
    }

}
