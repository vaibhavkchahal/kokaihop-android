package com.kokaihop.recipe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class RecipeResponse {

    @SerializedName("searchResults")
    private List<RecipeDetails> recipeDetailsList;
    @SerializedName("count")
    private int count;

    public List<RecipeDetails> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<RecipeDetails> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
