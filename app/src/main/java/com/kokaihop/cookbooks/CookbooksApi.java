package com.kokaihop.cookbooks;

import com.kokaihop.cookbooks.model.CookbookName;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 27/6/17.
 */

public interface CookbooksApi {

    @GET("v1/api/recipeCollections/listRecipes")
    Call<ResponseBody> getRecipeOfCookbook(@Query("collectionFriendlyUrl") String collectionFriendlyUrl,
                                           @Query("userFriendlyUrl") String userFriendlyUrl,
                                           @Query("offset") int offset,
                                           @Query("max") int max);

    @POST("v1/api/recipeCollections")
    Call<ResponseBody> createCookbook(@Header("Authorization") String accessToken,
                                      @Body CookbookName cookbook);

    @DELETE("v1/api/recipeCollections/{cookbookId}")
    Call<ResponseBody> deleteCookbook(@Header("Authorization") String accessToken,
                                      @Path("cookbookId") String cookbookId);

}
