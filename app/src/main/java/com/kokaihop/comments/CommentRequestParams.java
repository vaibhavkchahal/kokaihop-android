package com.kokaihop.comments;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class CommentRequestParams {

    private int offset, max;
    private String recipeId, typeFilter;
    private boolean allReciepeDetailNeeded;

    public CommentRequestParams() {
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }

    public int getOffset() {
        return offset;
    }

    public int getMax() {
        return max;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getTypeFilter() {
        return typeFilter;
    }

    public boolean isAllReciepeDetailNeeded() {
        return allReciepeDetailNeeded;
    }

    public void setAllReciepeDetailNeeded(boolean allReciepeDetailNeeded) {
        this.allReciepeDetailNeeded = allReciepeDetailNeeded;
    }
}
