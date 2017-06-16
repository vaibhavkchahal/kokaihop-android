package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class ListHeading {
    private String title;
    private long commentCount;
    private String recipeId;

    public ListHeading(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public long getCommentCount() {
        return commentCount;
    }

}
