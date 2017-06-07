package com.kokaihop.editprofile;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class SettingsApiHelper {
    private SettingsApi settingsApi;

    public SettingsApiHelper() {
        this.settingsApi = RetrofitClient.getInstance().create(SettingsApi.class);
    }


    public void changePassword(String accessToken, String userId, UserPassword password, final IApiRequestComplete successInterface){
        Call<SettingsResponse> userResponseCall = settingsApi.changePassword(accessToken,userId,password);
        userResponseCall.enqueue(new ResponseHandler<SettingsResponse>(successInterface));
    }
    public void changePreferences(String accessToken, String userId, EmailPreferences emailPreferences, final IApiRequestComplete successInterface){
        Call<SettingsResponse> userResponseCall = settingsApi.changePreferences(accessToken,userId,emailPreferences);
        userResponseCall.enqueue(new ResponseHandler<SettingsResponse>(successInterface));
    }

}
