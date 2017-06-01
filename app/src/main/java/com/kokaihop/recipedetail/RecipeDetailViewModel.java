package com.kokaihop.recipedetail;

import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {

    private final int COMMENTS_TO_LOAD = 3;

    public RecipeDetailViewModel(String recipeFriendlyUrl) {
        getRecipeDetails(recipeFriendlyUrl, COMMENTS_TO_LOAD);
    }

    private void getRecipeDetails(String recipeFriendlyUrl, int commentToLoad) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);

        Call<ResponseBody> myCall = recipeDetailApi.getRecipeDetails(recipeFriendlyUrl,commentToLoad);
        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("response recipe detail",""+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });
    }

    @Override
    protected void destroy() {
    }
}
