package com.kokaihop.feed;

import android.content.Context;
import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.Recipe;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.ApiConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class MainCourseViewModel extends BaseViewModel implements RecipeDataManager.RecipeDataListener {

    private int offset = 0;
    private int max = 20;
    private boolean isLike = true;
    private int recipeCount;
    RecipeDataManager dataManager = null;

    private List<Recipe> recipeList = new ArrayList<>();

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
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

    public List<Recipe> getRecipeDetailsList() {
        return recipeList;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeDetailsList(List<Recipe> recipeDetailsList) {
        this.recipeList = recipeDetailsList;
    }

    private void addRecipe(Recipe recipeInfo) {
        recipeList.add(recipeInfo);
    }


    public MainCourseViewModel(Context context) {
        dataManager = new RecipeDataManager(this);
        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);

        getRecipes(offset);
    }

    public void getRecipes(int offset) {
        setOffset(offset);
        setProgressVisible(true);
        new FeedApiHelper().getRecepies(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name(), isLike, getOffset(), max, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                setRecipeCount(response.getCount());
                List<Recipe> mRecipeList = new ArrayList<>();
                for (RecipeInfo recipeInfo : response.getRecipeDetailsList()) {
                    mRecipeList.add(recipeInfo.getRecipe());
                    Log.d("id", recipeInfo.getRecipe().get_id());
                }
                dataManager.insertOrUpdateData(mRecipeList);
                List<Recipe> recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
                setProgressVisible(false);
                setRecipeDetailsList(recipeList);
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(RecipeResponse response) {
                setProgressVisible(false);
            }
        });

    }

    @Override
    public void onTransactionComplete(boolean executed) {
        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
        //TODO: update Recycler view
    }




}
