package com.kokaihop.authentication;

import android.content.Context;

import com.kokaihop.authentication.forgotpassword.ForgotApiResponse;
import com.kokaihop.city.SignUpRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.network.ResponseHandler;

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
        Call<AuthenticationApiResponse> loginApiResponseCall = authenticationApi.doLogin(email, password);
        loginApiResponseCall.enqueue(new ResponseHandler<AuthenticationApiResponse>(successInterface));
    }


    public void doForgot(String email, final IApiRequestComplete successInterface) {
        Call<ForgotApiResponse> forgotApiResponseCall = authenticationApi.forgot(email);
        forgotApiResponseCall.enqueue(new ResponseHandler<ForgotApiResponse>(successInterface));
    }

    public void signup(SignUpRequest signUpRequest, final IApiRequestComplete successInterface) {
        Call<AuthenticationApiResponse> signUpApiResponseCall = authenticationApi.signUp(signUpRequest);
        signUpApiResponseCall.enqueue(new ResponseHandler<AuthenticationApiResponse>(successInterface));
    }

}
