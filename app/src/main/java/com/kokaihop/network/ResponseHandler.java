package com.kokaihop.network;


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
                    iApiRequestComplete.onFailure("Not a Registered user!");
                    break;
                case 422:
                    iApiRequestComplete.onFailure("User Already Exists!");
                    break;
                default:
                    iApiRequestComplete.onFailure("Something went wrong, try again!");
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        iApiRequestComplete.onFailure(t.getMessage());
    }
}
