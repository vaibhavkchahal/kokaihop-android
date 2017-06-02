package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeComment {

    private int imageId;
    private String userName;
    private String commentText;

    public RecipeComment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
