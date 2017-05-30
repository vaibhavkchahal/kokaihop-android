package com.kokaihop.database;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeInfo extends BaseObservable{
    @SerializedName("recipe")
    private RecipeRealmObject recipeRealmObject;

    public RecipeRealmObject getRecipeRealmObject() {
        return recipeRealmObject;
    }

    public void setRecipeRealmObject(RecipeRealmObject recipeRealmObject) {
        this.recipeRealmObject = recipeRealmObject;
    }
}
