package com.kokaihop.recipedetail;

import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {

    private final int COMMENTS_TO_LOAD = 3;

    private List<Object> recipeDetailItemsList = new ArrayList<>();

    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public void setRecipeDetailItemsList(List<Object> recipeDetailItemsList) {
        this.recipeDetailItemsList = recipeDetailItemsList;
    }

    public RecipeDetailViewModel(String recipeFriendlyUrl) {
        getRecipeDetails(recipeFriendlyUrl, COMMENTS_TO_LOAD);
    }

    private void getRecipeDetails(String recipeFriendlyUrl, int commentToLoad) {
        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);
        Call<ResponseBody> myCall = recipeDetailApi.getRecipeDetails(recipeFriendlyUrl, commentToLoad);
        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                prepareRecipeDetailList();
                try {
                    Log.i("response recipe detail", "" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });
    }

    private void prepareRecipeDetailList() {
        recipeDetailItemsList.add(new RecipeDetailHeader());
        recipeDetailItemsList.add(new AdvtDetail());
        recipeDetailItemsList.add(new ListHeading("Ingredients"));
        for (int i = 0; i < 10; i++) {
            RecipeDetailIndgredient indgredient = new RecipeDetailIndgredient(i + 1, "smor" + i, false);
            recipeDetailItemsList.add(indgredient);
        }
        recipeDetailItemsList.add(new RecipeQuantityVariator());
        recipeDetailItemsList.add(new AdvtDetail());
        recipeDetailItemsList.add(new ListHeading("Direction"));
        for (int i = 0; i < 10; i++) {
            recipeDetailItemsList.add(new RecipeCookingDirection("skar kotter i cm " + i));
        }
        recipeDetailItemsList.add(new RecipeSpecifications());
        for (int i = 0; i < 15; i++) {
            recipeDetailItemsList.add(new RecipeComment("User " + i, "comment " + i));
        }
        recipeDetailItemsList.add(new ListHeading("SimilarRecipies"));
        for (int i = 0; i < 5; i++) {
            recipeDetailItemsList.add(new SimilarRecipe());
        }

    }

    @Override
    protected void destroy() {
    }
}
