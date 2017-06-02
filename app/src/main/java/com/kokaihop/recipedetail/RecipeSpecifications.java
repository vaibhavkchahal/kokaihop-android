package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeSpecifications {
    private String name;
    private long dateCreated;
    private String category1;
    private String category2;
    private String category3;

    private String recipeLikes;
    private String viewerCount,printed,commentCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getRecipeLikes() {
        return recipeLikes;
    }

    public void setRecipeLikes(String recipeLikes) {
        this.recipeLikes = recipeLikes;
    }

    public String getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(String viewerCount) {
        this.viewerCount = viewerCount;
    }

    public String getPrinted() {
        return printed;
    }

    public void setPrinted(String printed) {
        this.printed = printed;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
