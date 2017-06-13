package com.kokaihop.editprofile;

import com.kokaihop.city.CityDetails;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.utility.Logger;

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

    public void changeProfilePicture(String accessToken, String userId, CloudinaryImage cloudinaryImage, final IApiRequestComplete successInterface){
        Call<SettingsResponse> userResponseCall = settingsApi.changeProfilePic(accessToken,userId,cloudinaryImage);
        userResponseCall.enqueue(new ResponseHandler<SettingsResponse>(successInterface));
    }

    public void changeCity(String accessToken, String userId, CityDetails cityDetails, final IApiRequestComplete successInterface){
        Call<SettingsResponse> userResponseCall = settingsApi.changeCity(accessToken,userId,cityDetails);
        userResponseCall.enqueue(new ResponseHandler<SettingsResponse>(successInterface));
    }

}
