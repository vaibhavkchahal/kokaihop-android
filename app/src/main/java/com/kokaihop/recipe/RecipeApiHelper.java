package com.kokaihop.recipe;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class RecipeApiHelper {
    RecipeApi recipeApi;

    public RecipeApiHelper() {
        recipeApi = RetrofitClient.getInstance().create(RecipeApi.class);
    }

    public void getRecipe(RecipeRequestParams recipeRequestParams, final IApiRequestComplete successCallback) {
        Call<SearchResponse> loginApiResponseCall = recipeApi.getRecipe(recipeRequestParams.getFetchFacets(), recipeRequestParams.getMax(),
                recipeRequestParams.getOffset(), recipeRequestParams.getSortParams(),
                recipeRequestParams.getType());
        loginApiResponseCall.enqueue(new ResponseHandler<SearchResponse>(successCallback));
    }

    public void getLatestRecipes(RecipeRequestParams recipeRequestParams, final IApiRequestComplete successCallback) {
        Call<ResponseBody> loginApiResponseCall = recipeApi.getLatestRecipes(recipeRequestParams.getFetchFacets(),
                recipeRequestParams.getMax(),
                recipeRequestParams.getOffset(),
                recipeRequestParams.getSortParams(),
                recipeRequestParams.getType(),
                recipeRequestParams.getTimeStamp());
        loginApiResponseCall.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }



}
