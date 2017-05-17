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

import static com.kokaihop.utility.Constants.AUTHORIZATION_BEARER;


/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class MainCourseViewModel extends BaseViewModel implements RecipeDataManager.RecipeDataListener {

    private int offset = 0;
    private int max = 20;
    private boolean isLike = true;
    private int recipeCount;
    private RecipeDataManager dataManager = null;
    private Context context;

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

    public List<Recipe> getRecipeList() {
        return recipeList;
    }


    public MainCourseViewModel(Context context) {
        this.context = context;
        dataManager = new RecipeDataManager(this);
        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);

        if (recipeList != null) {

        }
        getRecipes(offset);
    }

    public void getRecipes(int offset) {
        setOffset(offset);
        setProgressVisible(true);
//        String authorizationToken = AUTHORIZATION_BEARER+AppUtility.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String authorizationToken = AUTHORIZATION_BEARER + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        new FeedApiHelper().getRecepies(authorizationToken, ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name(), isLike, getOffset(), max, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                setRecipeCount(response.getCount());
                List<Recipe> mRecipeList = new ArrayList<>();
                for (RecipeInfo recipeInfo : response.getRecipeDetailsList()) {
                    mRecipeList.add(recipeInfo.getRecipe());
                    Log.d("id", recipeInfo.getRecipe().get_id());
                }
                dataManager.insertOrUpdateData(mRecipeList);
                recipeList= dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
                setProgressVisible(false);
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

    public void fetchRecipe() {
        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);

    }

    @Override
    public void onTransactionComplete(boolean executed) {
//        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
        //TODO: update Recycler view
    }


    @Override
    protected void destroy() {

    }
}
