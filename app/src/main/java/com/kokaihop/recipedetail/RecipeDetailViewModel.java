package com.kokaihop.recipedetail;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {

    private final int COMMENTS_TO_LOAD = 100;
    private RecipeRealmObject recipeRealmObject;
    private RecipeDataManager recipeDataManager;
    private String recipeID;
    private List<Object> recipeDetailItemsList = new ArrayList<>();

    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public void setRecipeDetailItemsList(List<Object> recipeDetailItemsList) {
        this.recipeDetailItemsList = recipeDetailItemsList;
    }

    public RecipeDetailViewModel(String recipeID) {
        this.recipeID = recipeID;
        recipeDataManager = new RecipeDataManager();
        recipeRealmObject = recipeDataManager.fetchRecipe(recipeID);
        getRecipeDetails(recipeRealmObject.getFriendlyUrl(), COMMENTS_TO_LOAD);
    }

    private void getRecipeDetails(final String recipeFriendlyUrl, int commentToLoad) {
        new RecipeDetailApiHelper().getRecipeDetail(recipeFriendlyUrl, commentToLoad, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                try {
                    ResponseBody responseBody = (ResponseBody) response;
                    final JSONObject json = new JSONObject(responseBody.string());
                    JSONObject recipeJSONObject = json.getJSONObject("recipe");
                    recipeDataManager.insertOrUpdateRecipeDetails(recipeJSONObject);
                    fetchSimilarRecipe(recipeFriendlyUrl,5,recipeDataManager.fetchRecipe(recipeID).getTitle());
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onError(Object response) {

            }
        });

    }


    private void fetchSimilarRecipe(String recipeFriendlyUrl, int limit, String title) {
        //https://staging-kokaihop.herokuapp.com/v1/api/recipes/getSimilarRecipes?friendlyUrl=varldens-enklaste-kyckling-i-ugn&limit=5&title=Världens enklaste kyckling i ugn

        new RecipeDetailApiHelper().getSimilarRecipe(recipeFriendlyUrl, limit, title, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {

                ResponseBody responseBody = (ResponseBody) response;
                try {
                     JSONObject json = new JSONObject(responseBody.string());
                    JSONArray recipeJSONArray = json.getJSONArray("similarRecipes");
                    recipeDataManager.updateSimilarRecipe(recipeID, recipeJSONArray);
                    recipeRealmObject = recipeDataManager.fetchRecipe(recipeID);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onError(Object response) {

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
