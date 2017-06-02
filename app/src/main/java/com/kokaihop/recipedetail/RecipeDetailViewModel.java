package com.kokaihop.recipedetail;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {

    private final int COMMENTS_TO_LOAD = 100;
    private RecipeRealmObject recipeRealmObject;
    private RecipeDataManager recipeDataManager;
    private  String recipeID;

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

    @Override
    protected void destroy() {
    }
}
