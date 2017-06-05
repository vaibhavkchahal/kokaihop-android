package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 5/6/17.
 */
public class RecipeDescription extends RealmObject{

    @SerializedName("long")
    private String recipeDescription;

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String description) {
        this.recipeDescription = description;
    }
}
