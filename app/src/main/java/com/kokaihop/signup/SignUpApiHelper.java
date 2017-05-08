package com.kokaihop.signup;

import android.content.Context;

import com.kokaihop.login.ForgotApiResponse;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.ResponseHandler;

import retrofit2.Call;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class SignUpApiHelper {
    private Context context;

    public SignUpApiHelper(Context context) {
        this.context = context;
    }

    public void doLogin(String email, String password, final IApiRequestComplete successInterface) {
        SignUpApiInterface loginApiInterface = RetrofitClient.getInstance().create(SignUpApiInterface.class);
        Call<SignUpApiResponse> loginApiResponseCall = loginApiInterface.doLogin(email, password);
        loginApiResponseCall.enqueue(new ResponseHandler<SignUpApiResponse>(successInterface));
    }


    public void doForgot(String email, final IApiRequestComplete successInterface) {
        SignUpApiInterface loginApiInterface = RetrofitClient.getInstance().create(SignUpApiInterface.class);
        Call<ForgotApiResponse> forgotApiResponseCall = loginApiInterface.forgot(email);
        forgotApiResponseCall.enqueue(new ResponseHandler<ForgotApiResponse>(successInterface));
    }

}
