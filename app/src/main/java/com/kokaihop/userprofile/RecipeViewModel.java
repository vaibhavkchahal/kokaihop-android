package com.kokaihop.userprofile;

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
 * Created by Rajendra Singh on 22/5/17.
 */

public class RecipeViewModel extends BaseViewModel {

    private Fragment fragment;
    private Context context;
    private String userId;
    private boolean isDownloading = true;
    private int max = 20;
    private int offset = 0;
    private int totalRecipes;
    private ProfileDataManager profileDataManager;
    private User user;

    public RecipeViewModel(Context context) {
        this.context = context;
    }

    public RecipeViewModel(Fragment fragment, Context context, String userId, User user) {
        this.max = 20;
        this.offset = 0;
        this.fragment = fragment;
        this.context = context;
        profileDataManager = new ProfileDataManager();
        this.user = user;
        this.userId = userId;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalRecipes() {
        return totalRecipes;
    }

    public void setTotalRecipes(int totalRecipes) {
        this.totalRecipes = totalRecipes;
    }
//Getting list of following users through api call

    public void getRecipesOfUsers(final int offset) {
        setProgressVisible(true);
        fetchRecipesFromDB();
//        final String userId = "56387aa81e443c0300c5a4b5";
        setOffset(offset);
        if (isDownloading) {
            new ProfileApiHelper().getRecipesOfUser(getUserId(), getOffset(), getMax(), new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    ResponseBody responseBody = (ResponseBody) response;
                    try {
                        final JSONObject jsonObject = new JSONObject(responseBody.string());
                        JSONArray recipes = jsonObject.getJSONArray("recipes");
                        profileDataManager.insertOrUpdateRecipeObjects(recipes, getUserId());
                        setTotalRecipes(jsonObject.getInt("count"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (getOffset() + getMax() >= getTotalRecipes()) {
                        setDownloading(false);
                    }
                    fetchRecipesFromDB();
                    setProgressVisible(false);
                }

                @Override
                public void onFailure(String message) {
                    Logger.e("Error", "Recipe not found");
                    setDownloading(false);
                    ((RecipeFragment) fragment).showUserProfile();
                    setProgressVisible(false);
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    ((RecipeFragment) fragment).showUserProfile();
                    setProgressVisible(false);
                }
            });
        }

    }

    //    get the recipes list from the database.
    public void fetchRecipesFromDB() {
        ArrayList<Recipe> recipeList = profileDataManager.getRecipesOfUser(userId);
        user.getRecipesList().clear();
        user.getRecipesList().addAll(recipeList);
        if (fragment.isVisible())
            ((RecipeFragment) fragment).showUserProfile();
    }

    @Override
    protected void destroy() {
        ((Activity) context).finish();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
