package com.kokaihop.recipe;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.RecipeRealmObject;

import java.util.List;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class SearchResponse {

    @SerializedName("searchResults")
    private List<RecipeRealmObject> recipeRealmObjects;
    @SerializedName("count")
    private int count;

    public List<RecipeRealmObject> getRecipeRealmObjects() {
        return recipeRealmObjects;
    }

    public void setRecipeRealmObjects(List<RecipeRealmObject> recipeRealmObjects) {
        this.recipeRealmObjects = recipeRealmObjects;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
