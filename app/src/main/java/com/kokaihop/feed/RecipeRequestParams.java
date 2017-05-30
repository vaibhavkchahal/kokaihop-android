package com.kokaihop.feed;

/**
 * Created by Vaibhav Chahal on 18/5/17.
 */

public class RecipeRequestParams {

    private String authorization, badgeType;
    private boolean isLike;
    private int offset, max;

    public RecipeRequestParams(String authorization, String badgeType, boolean isLike, int offset, int max) {
        this.authorization = authorization;
        this.badgeType = badgeType;
        this.isLike = isLike;
        this.offset = offset;
        this.max = max;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getBadgeType() {
        return badgeType;
    }


    public boolean isLike() {
        return isLike;
    }


    public int getOffset() {
        return offset;
    }


    public int getMax() {
        return max;
    }

}
