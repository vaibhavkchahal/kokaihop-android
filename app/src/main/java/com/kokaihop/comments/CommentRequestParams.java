package com.kokaihop.comments;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class CommentRequestParams {

    private int offset,max;
    private String recipeId,typeFilter;

    public CommentRequestParams(int offset, int max, String recipeId, String typeFilter) {
        this.offset = offset;
        this.max = max;
        this.recipeId = recipeId;
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
}
