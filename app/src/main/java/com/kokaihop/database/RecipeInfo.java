package com.kokaihop.database;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeInfo extends BaseObservable{
    @SerializedName("recipe")
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
