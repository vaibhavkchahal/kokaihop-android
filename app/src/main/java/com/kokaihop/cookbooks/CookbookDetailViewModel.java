package com.kokaihop.cookbooks;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.feed.Recipe;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 27/6/17.
 */

public class CookbookDetailViewModel extends BaseViewModel {


    private Fragment fragment;
    private Context context;
    private int offset, max, totalRecipes;
    private boolean isDownloading;
    private CookbooksDataManager dataManager;
    private User user;

    public CookbookDetailViewModel(Fragment fragment, Context context, User user) {
        this.fragment = fragment;
        this.context = context;
        this.user = user;
        isDownloading = true;
        dataManager = new CookbooksDataManager();
        max = 20;
    }

    public void getRecipesOfCookbook(final String cookbookFriendlyUrl, final String userFriendlyUrl, int offset) {

        fetchRecipesOfCookbooksFromDB(userFriendlyUrl, cookbookFriendlyUrl);
        setOffset(offset);
        setProgressVisible(true);
        if (isDownloading) {
            new CookbooksApiHelper().getRecipesOfCookbook(cookbookFriendlyUrl, userFriendlyUrl, getOffset(), getMax(), new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {

                    ResponseBody responseBody = (ResponseBody) response;
                    try {
                        final JSONObject jsonObject = new JSONObject(responseBody.string());
                        JSONArray recipes = jsonObject.getJSONArray("recipes");
                        dataManager.insertOrUpdateRecipeIntoCookbooks(recipes, userFriendlyUrl, cookbookFriendlyUrl);
                        setTotalRecipes(jsonObject.getInt("total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (getOffset() + getMax() >= getTotalRecipes()) {
                        setDownloading(false);
                    }
                    setProgressVisible(false);
                    fetchRecipesOfCookbooksFromDB(userFriendlyUrl, cookbookFriendlyUrl);
                }

                @Override
                public void onFailure(String message) {
                    Logger.e("Error", "Recipe not found");
                    setDownloading(false);
                    setProgressVisible(false);
                    ((CookbookDetailFragment) fragment).showCookbookDetails();
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    setProgressVisible(false);
                    ((CookbookDetailFragment) fragment).showCookbookDetails();
                }
            });
        }

    }

    //    get the recipes list from the database.
    public void fetchRecipesOfCookbooksFromDB(String userFriendlyUrl, String cookbookFriendlyUrl) {
        ArrayList<Recipe> recipeList = dataManager.getRecipesOfCookbook(userFriendlyUrl, cookbookFriendlyUrl);
        user.getRecipesList().clear();
        user.getRecipesList().addAll(recipeList);
        ((CookbookDetailFragment) fragment).showCookbookDetails();
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTotalRecipes() {
        return totalRecipes;
    }

    public void setTotalRecipes(int totalRecipes) {
        this.totalRecipes = totalRecipes;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }
}
