package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class SimilarRecipe {

    private int recipeImageId;
    private int userImageId;
    private String userName;
    private String recipeName;

    public int getRecipeImageId() {
        return recipeImageId;
    }

    public void setRecipeImageId(int recipeImageId) {
        this.recipeImageId = recipeImageId;
    }

    public int getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(int userImageId) {
        this.userImageId = userImageId;
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
}
