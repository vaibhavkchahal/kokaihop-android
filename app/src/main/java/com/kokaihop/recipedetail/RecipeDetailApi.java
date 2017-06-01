package com.kokaihop.recipedetail;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public interface RecipeDetailApi {

    @FormUrlEncoded
    @POST("v1/api/recipes/{recipefriendlyUrl}/app")
    Call<ResponseBody> getRecipeDetails(@Path("recipefriendlyUrl") String recipefriendlyUrl, @Field("limit") int commentCount);

}