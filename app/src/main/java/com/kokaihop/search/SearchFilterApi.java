package com.kokaihop.search;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public interface SearchFilterApi {

    @GET("v1/api/cuisines")
    Call<ResponseBody> cuisines();

    @GET("v1/api/cookingMethods")
    Call<ResponseBody> cookingMethods();

    @GET("v1/api/recipeCategories")
    Call<ResponseBody> recipeCategories();
}
