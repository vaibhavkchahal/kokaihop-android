package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class SimilarRecipe {

    private String recipeId;
    private String recipeImageUrl;
    private String userImageUrl;
    private String userName;
    private String recipeName;
    private String userFriendlyUrl;
    private String userId;

    public SimilarRecipe(String recipeId, String recipeName, String recipeImageId, String userImageId, String userName, String userFriendlyUrl, String userId) {
        this.recipeImageUrl = recipeImageId;
        this.userImageUrl = userImageId;
        this.userName = userName;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.userFriendlyUrl = userFriendlyUrl;
        this.userId = userId;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserFriendlyUrl() {
        return userFriendlyUrl;
    }

    public void setUserFriendlyUrl(String userFriendlyUrl) {
        this.userFriendlyUrl = userFriendlyUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
