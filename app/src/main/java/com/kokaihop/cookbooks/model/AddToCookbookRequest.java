package com.kokaihop.cookbooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 5/7/17.
 */

public class AddToCookbookRequest {

    @SerializedName("recipeId")
    private String recipeId;

    @SerializedName("collectionId")
    private String collectionId;

    public AddToCookbookRequest(String recipeId, String collectionId) {
        this.recipeId = recipeId;
        this.collectionId = collectionId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
}
