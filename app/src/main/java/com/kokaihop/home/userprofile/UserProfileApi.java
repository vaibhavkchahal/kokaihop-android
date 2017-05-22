package com.kokaihop.home.userprofile;

import com.kokaihop.home.userprofile.model.FollowingApiResponse;
import com.kokaihop.home.userprofile.model.UserApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public interface UserProfileApi {

    @GET("v1/api/users/me")
    Call<UserApiResponse> getUserData(@Header("Authorization") String authorization,
                                      @Query("languageCode") String languageCode);

    @GET("v1/api/users/{userId}/following")
    Call<FollowingApiResponse> getUserFollowing(@Header("Authorization") String authorization,
                                                @Path("userId") String userId,
                                                @Query("max") int max,
                                                @Query("offset") int offset);
}