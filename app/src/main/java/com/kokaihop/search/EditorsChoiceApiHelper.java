package com.kokaihop.search;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class EditorsChoiceApiHelper {

    public void getEditorsChoice(String section, int maxLength, String type, final IApiRequestComplete successCallback) {
        EditorsChoiceApi editorsChoiceApi = RetrofitClient.getInstance().create(EditorsChoiceApi.class);
        Call<ResponseBody> editorsChoiceCall = editorsChoiceApi.getEditorsChoice(section, maxLength, type);
        editorsChoiceCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }
}
