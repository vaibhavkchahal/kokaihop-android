package com.kokaihop.feed;

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

public class MainCourseViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 100;
    private boolean isLike = true;
    private String badgeType = "MAIN_COURSE_OF_THE_DAY";


    public MainCourseViewModel() {
        getRecipes();
    }

    public void getRecipes() {
        setProgressVisible(true);
        new FeedApiHelper().getRecepies(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name(), isLike, offset, max, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {

                RecipeDataManager dataManager = new RecipeDataManager();
                List<Recipe> mRecipeList = new ArrayList<Recipe>();
                for (RecipeInfo recipeInfo : response.getRecipeDetailsList()) {
                    mRecipeList.add(recipeInfo.getRecipe());
                }
                dataManager.insertOrUdpateData(mRecipeList);


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
}
