package com.kokaihop.network;


import com.altaworks.kokaihop.ui.R;
import com.kokaihop.KokaihopApplication;

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
                    iApiRequestComplete.onFailure(KokaihopApplication.getContext().getResources().getString(R.string.incorrect_email_passwd));
                    break;
                case 422:
                    iApiRequestComplete.onFailure(KokaihopApplication.getContext().getResources().getString(R.string.user_already_exists));
                    break;
                default:
                    iApiRequestComplete.onFailure(KokaihopApplication.getContext().getResources().getString(R.string.something_went_wrong_msg));
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof IOException)
            iApiRequestComplete.onFailure(KokaihopApplication.getContext().getResources().getString(R.string.check_intenet_connection));
        else
            iApiRequestComplete.onFailure(KokaihopApplication.getContext().getResources().getString(R.string.something_went_wrong_msg));
    }
}
