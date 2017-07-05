package com.kokaihop.recipedetail;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class RecipeDetailApiHelper {

    public void getRecipeDetail(String authorization,String recipeFriendlyUrl, int commentCount, final IApiRequestComplete successCallback) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);
        Call<ResponseBody> recipeDetailResponseCall = recipeDetailApi.getRecipeDetails(authorization,recipeFriendlyUrl, commentCount);
        recipeDetailResponseCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }


    public void getSimilarRecipe(String recipeFriendlyUrl, int limit, String title, final IApiRequestComplete successCallback) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);
        Call<ResponseBody> recipeDetailResponseCall = recipeDetailApi.getSimilarRecipe(recipeFriendlyUrl, limit, title);
        recipeDetailResponseCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));

    }

    public void rateRecipe(String accessToken, RatingRequestParams requestParams, final IApiRequestComplete successCallback) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);
        Call<ResponseBody> recipeDetailResponseCall = recipeDetailApi.rateRecipe(accessToken, requestParams);
        recipeDetailResponseCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));

    }
    public void updateRecipeDetail(String accessToken, String recipeId, RequestBody recipe, final IApiRequestComplete successCallback) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);
        Call<ResponseBody> recipeDetailResponseCall = recipeDetailApi.updateRecipeDetail(accessToken,recipeId, recipe);
        recipeDetailResponseCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }
}
