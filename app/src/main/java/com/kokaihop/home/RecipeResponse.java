package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class RecipeResponse {

   @SerializedName("searchResults")
    private List<RecipeDetails> recipeDetailsList;

    public List<RecipeDetails> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<RecipeDetails> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
    }
}
