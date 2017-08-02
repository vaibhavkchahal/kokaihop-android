package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeSpecifications {
    private String name;
    private String ImageId;
    private long dateCreated;
    private String category1, category2, category3;
    private String category1FriendlyUrl, category2FriendlyUrl, category3FriendlyUrl;

    private String userId, friendlyUrl;

    private long viewerCount, printed, addToCollections;

    public long getAddToCollections() {
        return addToCollections;
    }

    public void setAddToCollections(long addToCollections) {
        this.addToCollections = addToCollections;
    }

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

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

    public long getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(long viewerCount) {
        this.viewerCount = viewerCount;
    }

    public long getPrinted() {
        return printed;
    }

    public void setPrinted(long printed) {
        this.printed = printed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public String getCategory1FriendlyUrl() {
        return category1FriendlyUrl;
    }

    public void setCategory1FriendlyUrl(String category1FriendlyUrl) {
        this.category1FriendlyUrl = category1FriendlyUrl;
    }

    public String getCategory2FriendlyUrl() {
        return category2FriendlyUrl;
    }

    public void setCategory2FriendlyUrl(String category2FriendlyUrl) {
        this.category2FriendlyUrl = category2FriendlyUrl;
    }

    public String getCategory3FriendlyUrl() {
        return category3FriendlyUrl;
    }

    public void setCategory3FriendlyUrl(String category3FriendlyUrl) {
        this.category3FriendlyUrl = category3FriendlyUrl;
    }
}
