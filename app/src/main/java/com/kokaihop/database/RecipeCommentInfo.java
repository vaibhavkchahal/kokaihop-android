package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeCommentInfo extends RealmObject {
    private String id, oldId, title, creatorName;
    private String mainImagePublicId;
    private float recipeRating;
    private int ratersCount;

    public String getId() {
        return id;
    }

    public String getOldId() {
        return oldId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getMainImagePublicId() {
        return mainImagePublicId;
    }

    public void setMainImagePublicId(String mainImagePublicId) {
        this.mainImagePublicId = mainImagePublicId;
    }

    public float getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(float recipeRating) {
        this.recipeRating = recipeRating;
    }

    public int getRatersCount() {
        return ratersCount;
    }

    public void setRatersCount(int ratersCount) {
        this.ratersCount = ratersCount;
    }
}
