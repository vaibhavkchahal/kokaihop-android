package com.kokaihop.editprofile;

import com.kokaihop.city.CityDetails;
import com.kokaihop.userprofile.model.CloudinaryImage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public interface SettingsApi {

    @PUT("v1/api/users/{userId}")
    Call<SettingsResponse> changePassword(@Header("Authorization") String authorization,
                                          @Path("userId") String userId,
                                          @Body UserPassword password);

    @PUT("v1/api/users/{userId}")
    Call<SettingsResponse> changePreferences(@Header("Authorization") String authorization,
                                             @Path("userId") String userId,
                                             @Body EmailPreferences emailPreferences);

    @PUT("v1/api/users/{userId}")
    Call<SettingsResponse> changeProfilePic(@Header("Authorization") String authorization,
                                             @Path("userId") String userId,
                                             @Body CloudinaryImage cloudinaryImage);

    @PUT("v1/api/users/{userId}")
    Call<SettingsResponse> changeCity(@Header("Authorization") String authorization,
                                             @Path("userId") String userId,
                                             @Body CityDetails city);


}