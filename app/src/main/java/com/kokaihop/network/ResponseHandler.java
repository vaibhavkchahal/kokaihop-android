package com.kokaihop.network;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vaibhav Chahal on 8/5/17.
 */

public class ResponseHandler<T> implements Callback<T> {

    private IApiRequestComplete iApiRequestComplete;

    public ResponseHandler(IApiRequestComplete iApiRequestComplete) {
        this.iApiRequestComplete = iApiRequestComplete;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            iApiRequestComplete.onSuccess(response.body());
        } else {
            switch (response.code()) {
                case 401:
                    iApiRequestComplete.onFailure("Email or password is incorrect. Please try again.");
                    break;
                case 422:
                    iApiRequestComplete.onFailure("A user with the email address you provided already exists");
                    break;
                default:
                    iApiRequestComplete.onFailure("Something went wrong, try again!");
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof IOException)
            iApiRequestComplete.onFailure("Check Your Internet Connection!");
        else
            iApiRequestComplete.onFailure("Something went wrong, try again!");
    }
}
