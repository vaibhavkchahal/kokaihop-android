package com.kokaihop.recipedetail;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public interface RecipeDetailApi {

    @FormUrlEncoded
    @POST("v1/api/recipes/{recipefriendlyUrl}/app")
    Call<ResponseBody> getRecipeDetails(@Path("recipefriendlyUrl") String recipefriendlyUrl, @Field("limit") int commentCount);

    @GET("v1/api/recipes/getSimilarRecipes")
    Call<ResponseBody> getSimilarRecipe(@Query("friendlyUrl") String recipeFriendlyUrl, @Query("limit") int limit, @Query("title") String title);

    @POST("v1/api/recipes/{recipeId}")
    Call<ResponseBody> updateRecipeDetail(@Header("Authorization") String authorization,
                                          @Path("recipeId") String recipeId,
                                          @Body RequestBody recipe);

}