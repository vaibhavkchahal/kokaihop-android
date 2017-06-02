package com.kokaihop.recipedetail;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

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
    private  String recipeID;
    private List<Object> recipeDetailItemsList = new ArrayList<>();

    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public void setRecipeDetailItemsList(List<Object> recipeDetailItemsList) {
        this.recipeDetailItemsList = recipeDetailItemsList;
    }

    public RecipeDetailViewModel(String recipeID) {
        this.recipeID=recipeID;
        recipeDataManager = new RecipeDataManager();
        recipeRealmObject = recipeDataManager.fetchRecipe(recipeID);
        getRecipeDetails(recipeRealmObject.getFriendlyUrl(), COMMENTS_TO_LOAD);
    }

    private void getRecipeDetails(String recipeFriendlyUrl, int commentToLoad) {
        new RecipeDetailApiHelper().getRecipeDetail(recipeFriendlyUrl, commentToLoad, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                try {
                    ResponseBody responseBody = (ResponseBody) response;
                    final JSONObject json = new JSONObject(responseBody.string());
                    recipeDataManager.insertOrUpdateRecipeDetails(json);
                    recipeRealmObject = recipeDataManager.fetchRecipe(recipeID);
                    Logger.i("badgeType",recipeRealmObject.getBadgeType());


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
//        RecipeDetailApi recipeDetailApi = RetrofitClient.getInstance().create(RecipeDetailApi.class);

//        Call<ResponseBody> myCall = recipeDetailApi.getRecipeDetails(recipeFriendlyUrl, commentToLoad);


      /*  myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("response recipe detail",""+response.body());

                try {
                   final JSONObject json = new JSONObject(response.body().string());
                    final JSONObject recipeJSONObject=json.getJSONObject("recipe");
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Realm.getDefaultInstance().createOrUpdateObjectFromJson(RecipeRealmObject.class, recipeJSONObject);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });*/
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
