package com.kokaihop.recipe;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class RecipeApiHelper {

    public void getRecipe(RecipeRequestParams recipeRequestParams, final IApiRequestComplete successCallback) {
        RecipeApi loginApiInterface = RetrofitClient.getInstance().create(RecipeApi.class);

        Call<SearchResponse> loginApiResponseCall = loginApiInterface.getRecipe(recipeRequestParams.getFetchFacets(), recipeRequestParams.getMax(),
                recipeRequestParams.getOffset(), recipeRequestParams.getSortParams(),
                recipeRequestParams.getType());
        loginApiResponseCall.enqueue(new ResponseHandler<SearchResponse>(successCallback));
    }
}
