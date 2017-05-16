package com.kokaihop.feed;

import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.Recipe;
import com.kokaihop.network.IApiRequestComplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class MainCourseViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 10;
    private boolean isLike = true;
    private String badgeType = "MAIN_COURSE_OF_THE_DAY";

    private List<Recipe> recipeDetailsList = new ArrayList<>();

    @Bindable
    public List<Recipe> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<Recipe> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
        notifyPropertyChanged(BR.recipeDetailsList);
    }

    public MainCourseViewModel() {
        getRecipes();
//        addItems();
    }

    public void getRecipes() {
        setProgressVisible(true);
        new FeedApiHelper().getRecepies(badgeType,isLike,offset,max,new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                setProgressVisible(false);
                setRecipeDetailsList(response.getRecipeDetailsList());
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

    public void addItems() {
        for (int i = 1; i < 51; ++i) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe->"+i);
            recipeDetailsList.add(recipe);
        }
    }
}
