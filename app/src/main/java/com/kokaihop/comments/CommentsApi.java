package com.kokaihop.comments;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public interface CommentsApi {

    @GET("v1/api/comments/app")
    Call<ResponseBody> getCommentsList(@Query("max") int max, @Query("offset") int offset,
                                       @Query("recipeId") String recipeId, @Query("typeFilter") String typeFilter);

}
