package com.kokaihop.home;

import android.content.Context;

import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.authentication.AuthenticationApiResponse;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipe.RecipeApiHelper;
import com.kokaihop.recipe.RecipeRequestParams;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 12/5/17.
 */

public class HomeViewModel extends BaseViewModel {
    Context context;

    public HomeViewModel(Context context) {
        this.context = context;
    }

    public void getLatestRecipes() {
        final RecipeRequestParams recipeRequestParams = getRecipeRequestParams();
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<RecipeRealmObject> recipeRealmObjects = realm.where(RecipeRealmObject.class).findAllSorted("dateCreated", Sort.DESCENDING);
        if (recipeRealmObjects.size() > 0) {
            RecipeRealmObject realmObject = recipeRealmObjects.first();
            recipeRequestParams.setTimeStamp(realmObject.getDateCreated());
            Logger.e("Latest Date", realmObject.getDateCreated() + "ms");
            new RecipeApiHelper().getLatestRecipes(recipeRequestParams, new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    ResponseBody responseBody = (ResponseBody) response;
                    try {
                        JSONObject json = new JSONObject(responseBody.string());
                        JSONArray recipeJSONArray = json.getJSONArray("searchResults");
                        if ((recipeJSONArray != null) && (recipeJSONArray.length() > 0)) {
                            new RecipeDataManager().insertOrUpdateRecipe(recipeJSONArray);
                            recipeRequestParams.setOffset(recipeRequestParams.getOffset() + recipeRequestParams.getMax());
                            if (recipeJSONArray.length() >= recipeRequestParams.getMax()) {
                                getLatestRecipes();
                            }
                        } else {
//                        Toast.makeText(context, R.string.recipes_updated, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message) {
//                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Object response) {
//                Toast.makeText(context, "Update Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //    seeting the argumenets for the recipeListUpdate api call.
    //    max 100 for maximum 100 elements at a time.
    //    setWithImages 0 for all the recipes with or without images, 1 for recipes with images only
    public RecipeRequestParams getRecipeRequestParams() {
        RecipeRequestParams recipeRequestParams = new RecipeRequestParams();
        recipeRequestParams.setSortParams(ApiConstants.MOST_RECENT);
        recipeRequestParams.setMax(100);
        recipeRequestParams.setFetchFacets(0);
        recipeRequestParams.setOffset(0);
        recipeRequestParams.setWithImages(0);
        recipeRequestParams.setType(ApiConstants.RecipeType.Recipe.name());
        return recipeRequestParams;
    }

    public void login(String userName, String password) {
        new AuthenticationApiHelper(context).doLogin(userName, password, new IApiRequestComplete<AuthenticationApiResponse>() {
            @Override
            public void onSuccess(AuthenticationApiResponse response) {
                setProgressVisible(false);
                SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                SharedPrefUtils.setSharedPrefStringData(context, Constants.USER_ID, response.getUserAuthenticationDetail().getId());
                SharedPrefUtils.setSharedPrefStringData(context, Constants.FRIENDLY_URL, response.getUserAuthenticationDetail().getFriendlyUrl());
                EventBus.getDefault().postSticky(new AuthUpdateEvent("updateRequired"));
                SharedPrefUtils.setSharedPrefStringData(context, Constants.USER_Email_PASSWORD, "");
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(AuthenticationApiResponse response) {
                setProgressVisible(false);
            }
        });
    }

    @Override
    protected void destroy() {

    }
}
