package com.kokaihop.feed;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.Recipe;

import java.util.List;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeResponse {

    @SerializedName("recipes")
    private List<Recipe> recipeDetailsList;
    @SerializedName("totalCount")
    private int count;

    /*@SerializedName("myLikes")
    private List count;
*/
    public List<Recipe> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<Recipe> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
