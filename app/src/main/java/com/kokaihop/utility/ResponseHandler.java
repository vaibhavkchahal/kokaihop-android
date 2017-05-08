package com.kokaihop.utility;


import com.kokaihop.network.IApiRequestComplete;

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
                case 400:
                    iApiRequestComplete.onFailure("Getting 400");
                    break;
                case 401:
                    iApiRequestComplete.onFailure("Getting 401");
                    break;
                case 404:
                    iApiRequestComplete.onFailure("Getting 404");
                    break;
                case 500:
                    iApiRequestComplete.onFailure("Getting 500");
                    break;
                default:
                    iApiRequestComplete.onFailure("default");
                    break;

            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        iApiRequestComplete.onFailure(t.getMessage());
    }
}
