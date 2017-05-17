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

    public void getRecepies(String authorization, String badgeType,boolean isLike,int offset,int max,final IApiRequestComplete successInterface) {
        Call<RecipeResponse> recipeResponseCall = feedApi.getRecepies(authorization,badgeType,isLike,offset,max);
        recipeResponseCall.enqueue(new ResponseHandler<RecipeResponse>(successInterface));
    }

}
