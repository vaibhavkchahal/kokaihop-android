package com.kokaihop.cookbooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 5/7/17.
 */

public class RemoveFromCookbookRequest {

    @SerializedName("collectionId")
    private String collectionId;

    @SerializedName("recipeIds")
    private String[] recipeIds;

    public RemoveFromCookbookRequest(String collectionId, String[] recipeIds) {
        this.collectionId = collectionId;
        this.recipeIds = recipeIds;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String[] getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(String[] recipeIds) {
        this.recipeIds = recipeIds;
    }
}
