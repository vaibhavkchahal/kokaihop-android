package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeDetailHeader {

    private float rating;
    private String title;
    private String badgeType;
    private String description;
    private String recipeId;
    private String creatorFriendlyUrl;

    public RecipeDetailHeader(float rating, String title, String badgeType, String description, String creatorFriendlyUrl) {
        this.rating = rating;
        this.title = title;
        this.badgeType = badgeType;
        this.description = description;
        this.creatorFriendlyUrl = creatorFriendlyUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCreatorFriendlyUrl() {
        return creatorFriendlyUrl;
    }

    public void setCreatorFriendlyUrl(String creatorFriendlyUrl) {
        this.creatorFriendlyUrl = creatorFriendlyUrl;
    }
}
