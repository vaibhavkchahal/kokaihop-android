package com.kokaihop.login;

import android.content.Context;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class LoginApiHelper {
    private Context context;

    public LoginApiHelper(Context context) {
        this.context = context;
    }

    public void doLogin(String email, String password, final IApiRequestComplete successInterface){
        LoginApiInterface loginApiInterface = RetrofitClient.getInstance().create(LoginApiInterface.class);
        Call<LoginApiResponse> loginApiResponseCall = loginApiInterface.doLogin(email,password);

        loginApiResponseCall.enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {
                successInterface.onSuccess(response);
            }

            @Override
            public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                successInterface.onFailure(t.getMessage());
            }
        });
    }

}
