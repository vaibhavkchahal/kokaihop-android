package com.kokaihop.comments;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public interface CommentsApi {

    @GET("v1/api/comments/app")
    Call<ResponseBody> getCommentsList(@Query("max") int max, @Query("offset") int offset,
                                       @Query("recipeId") String recipeId, @Query("typeFilter") String typeFilter);

    @GET("v1/api/comments/app")
    Call<ResponseBody> getCommentsList(@QueryMap Map<String, String> map);

   /* // just for showing comments directly from api.
    @GET("v1/api/comments/app")
    Call<HomeCommentsApiResponse> getHomeCommentsList(@QueryMap Map<String, String> map);
*/
    @POST("v1/api/comments")
    Call<ResponseBody> postComment(@Header("Authorization") String authorization,
                                   @Body PostCommentRequestParams request);

    @GET("v1/api/comments/{commentId}")
    Call<ResponseBody> getCommentInfo(@Path("commentId") String commentId);


}
