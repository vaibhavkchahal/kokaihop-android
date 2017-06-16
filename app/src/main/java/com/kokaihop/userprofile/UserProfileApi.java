package com.kokaihop.userprofile;

import com.kokaihop.database.UserRealmObject;
import com.kokaihop.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.userprofile.model.FollowingToggleResponse;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.userprofile.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public interface UserProfileApi {

    @GET("v1/api/users/me")
    Call<UserRealmObject> getUserData(@Header("Authorization") String authorization,
                                      @Query("languageCode") String languageCode);

    @GET("v1/api/users/profile")
    Call<User> getOtherUserData(@Header("Authorization") String authorization,
                                @Query("friendlyUrl") String friendlyUrl,
                                @Query("languageCode") String languageCode);

    @GET("v1/api/users/{userId}/following")
    Call<FollowingFollowersApiResponse> getFollowingUsers(@Header("Authorization") String authorization,
                                                          @Path("userId") String userId,
                                                          @Query("max") int max,
                                                          @Query("offset") int offset);

    @GET("v1/api/users/{userId}/followers")
    Call<FollowingFollowersApiResponse> getFollowers(@Header("Authorization") String authorization,
                                                     @Path("userId") String userId,
                                                     @Query("max") int max,
                                                     @Query("offset") int offset);

    @POST("v1/api/users/toggleFollowUser")
    Call<FollowingToggleResponse> toggleFollowing(@Header("Authorization") String authorization,
                                                  @Body ToggleFollowingRequest toggleFollowingRequest);

    @GET("v1/api/recipes/getRecipesOfUser")
    Call<ResponseBody> getRecipesOfUser(@Query("userId") String userId,
                                        @Query("offset") int offset,
                                        @Query("max") int max);

    @PUT("v1/api/users/updateLogoutDetails")
    Call<LogoutResponse> logoutUser(@Header("Authorization") String authorization);

}