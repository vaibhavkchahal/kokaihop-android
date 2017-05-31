package com.kokaihop.feed;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public interface FeedApi {
    // https://staging-kokaihop.herokuapp.com/v1/api/badges?badgeType=MAIN_COURSE_OF_THE_DAY&isLike=true&max=20&offset=0

    @GET("v1/api/badges")
    Call<RecipeResponse> getRecepies(@Header("Authorization") String authorization, @Query("badgeType") String badgeType, @Query("isLike") boolean isLike,
                                     @Query("offset") int offset, @Query("max") int max);

    @POST("v1/api/likes/app")
    Call<RecipeLikeApiResponse> updateRecipeLike(@Header("Authorization") String authorization,@Body RecipeLikeRequest likeRequest);
}
