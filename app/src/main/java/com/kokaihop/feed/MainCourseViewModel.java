package com.kokaihop.feed;

import android.databinding.Bindable;
import android.util.Log;

import com.altaworks.kokaihop.ui.BR;
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
    RecipeDataManager dataManager = null;

    private List<Recipe> recipeList = new ArrayList<>();

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

    public List<RecipeInfo> getRecipeDetailsList() {
        return recipeDetailsList;
    @Bindable
    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeDetailsList(List<RecipeInfo> recipeDetailsList) {
        this.recipeDetailsList.addAll(recipeDetailsList);
    }

    private void addRecipe(RecipeInfo recipeInfo) {
        recipeDetailsList.add(recipeInfo);
    }


    public MainCourseViewModel() {
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
                List<Recipe> mRecipeList = new ArrayList<>();
                for (RecipeInfo recipeInfo : response.getRecipeDetailsList()) {
                    mRecipeList.add(recipeInfo.getRecipe());
                    Log.d("id", recipeInfo.getRecipe().get_id());
                }
                dataManager.insertOrUpdateData(mRecipeList);

                List<Recipe> recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);


                setProgressVisible(false);
                setRecipeList(recipeList);
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
