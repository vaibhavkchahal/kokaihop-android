package com.kokaihop.feed;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class FeedApiHelper {
    private FeedApi feedApi;

    public FeedApiHelper() {
        this.feedApi = RetrofitClient.getInstance().create(FeedApi.class);
    }

    public void getRecepies(RecipeRequestParams params,final IApiRequestComplete successInterface) {
        Call<RecipeResponse> recipeResponseCall = feedApi.getRecepies(params.getAuthorization(),params.getBadgeType(),params.isLike(),params.getOffset(),params.getMax());
        recipeResponseCall.enqueue(new ResponseHandler<RecipeResponse>(successInterface));
    }

}
