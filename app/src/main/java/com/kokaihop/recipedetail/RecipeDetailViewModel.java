package com.kokaihop.recipedetail;

import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
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

    @Bindable
    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public void setRecipeDetailItemsList(List<Object> recipeDetailItemsList) {
        this.recipeDetailItemsList = recipeDetailItemsList;
        notifyPropertyChanged(BR.recipeDetailItemsList);
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
                    prepareRecipeDetailList(recipeRealmObject);

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

    private void prepareRecipeDetailList(RecipeRealmObject recipeRealmObject) {
        RecipeDetailHeader recipeDetailHeader = new RecipeDetailHeader(recipeRealmObject.getRatingRealmObject().getAverage(), recipeRealmObject.getTitle(), recipeRealmObject.getBadgeType(), recipeRealmObject.getStatus());
        recipeDetailItemsList.add(recipeDetailHeader);
        recipeDetailItemsList.add(new AdvtDetail());
        recipeDetailItemsList.add(new ListHeading("Ingredients"));
        for (int i = 0; i < recipeRealmObject.getIngredients().size(); i++) {
            recipeDetailItemsList.add(recipeRealmObject.getIngredients().get(i));
        }
        recipeDetailItemsList.add(new RecipeQuantityVariator(recipeRealmObject.getServings()));
        recipeDetailItemsList.add(new AdvtDetail());
        recipeDetailItemsList.add(new ListHeading("Direction"));
// for (int i = 0; i < recipeRealmObject.getCookingSteps().length; i++) {
// recipeDetailItemsList.add(new RecipeCookingDirection(recipeRealmObject.getCookingSteps()[i].getString()));
// }
        RecipeSpecifications recipeSpecifications = getRecipeSpecifications(recipeRealmObject);
        recipeDetailItemsList.add(recipeSpecifications);
        for (int i = 0; i < recipeRealmObject.getComments().size(); i++) {
            if (i > 2) {
                break;
            }
            recipeDetailItemsList.add(recipeRealmObject.getComments().get(i));
        }
        recipeDetailItemsList.add(new ListHeading("SimilarRecipies"));
        for (int i = 0; i < 5; i++) {
            recipeDetailItemsList.add(new SimilarRecipe());
        }

    }

    private RecipeSpecifications getRecipeSpecifications(RecipeRealmObject recipeRealmObject) {
        RecipeSpecifications specifications = new RecipeSpecifications();
        specifications.setName(recipeRealmObject.getCreatedBy().getName());
        specifications.setDateCreated(recipeRealmObject.getDateCreated());
        specifications.setCategory1(recipeRealmObject.getCookingMethod().getName());
        specifications.setCategory2(recipeRealmObject.getCuisine().getName());
        specifications.setCategory3(recipeRealmObject.getCategory().getName());
        specifications.setViewerCount(recipeRealmObject.getCounter().getViewed());
        specifications.setPrinted(recipeRealmObject.getCounter().getPrinted());
        specifications.setAddToCollections(recipeRealmObject.getCounter().getAddedToCollection());
        return specifications;
    }


    @Override
    protected void destroy() {
    }
}
