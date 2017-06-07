package com.kokaihop.editprofile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public interface EditUserProfileApi {

    @PUT("v1/api/users/{userId}")
    Call<EditProfileResponse> changePassword(@Header("Authorization") String authorization,
                                             @Path("userId") String userId,
                                             @Body UserPassword password);

    @PUT("v1/api/users/{userId}")
    Call<EditProfileResponse> changePreferences(@Header("Authorization") String authorization,
                                                @Path("userId") String userId,
                                                @Body EmailPreferences emailPreferences);


}