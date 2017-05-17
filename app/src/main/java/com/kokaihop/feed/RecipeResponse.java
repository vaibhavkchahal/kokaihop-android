package com.kokaihop.feed;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.RecipeInfo;

import java.util.List;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeResponse {

    @SerializedName("recipes")
    private List<RecipeInfo> recipeDetailsList;
    @SerializedName("totalCount")
    private int count;

    @SerializedName("myLikes")
    String myLikes[];

    public List<RecipeInfo> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<RecipeInfo> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
