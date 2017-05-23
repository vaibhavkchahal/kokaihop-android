package com.kokaihop.feed;

import com.kokaihop.feed.maincourse.RecipeLikeApiResponse;
import com.kokaihop.feed.maincourse.RecipeLikeRequest;
import com.kokaihop.feed.maincourse.RecipeRequestParams;
import com.kokaihop.feed.maincourse.RecipeResponse;
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

    public void getRecepies(RecipeRequestParams params, final IApiRequestComplete successInterface) {
        Call<RecipeResponse> recipeResponseCall = feedApi.getRecepies(params.getAuthorization(), params.getBadgeType(), params.isLike(), params.getOffset(), params.getMax());
        recipeResponseCall.enqueue(new ResponseHandler<RecipeResponse>(successInterface));
    }

    public void updateRecipeLike(String accessToken,RecipeLikeRequest request, final IApiRequestComplete successInterface) {
        Call<RecipeLikeApiResponse> call = feedApi.updateRecipeLike(accessToken,request);
        call.enqueue(new ResponseHandler<RecipeLikeApiResponse>(successInterface));
    }

}
