package com.kokaihop.feed;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class Recipe {

    private String _id;
    private String title;
    private String type;

    private String createdById;
    private String createdByName;
    private String createdByProfileImageId;
    private String mainImagePublicId;
    public boolean isFavorite;
    private long likes;
    private float ratingAverage;
    private long badgeDateCreated;
    private long comments;
    private String badgeType;
    private long lastUpdated;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByProfileImageId() {
        return createdByProfileImageId;
    }

    public void setCreatedByProfileImageId(String createdByProfileImageId) {
        this.createdByProfileImageId = createdByProfileImageId;
    }

    public String getMainImagePublicId() {
        return mainImagePublicId;
    }

    public void setMainImagePublicId(String mainImagePublicId) {
        this.mainImagePublicId = mainImagePublicId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public float getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(float ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public long getBadgeDateCreated() {
        return badgeDateCreated;
    }

    public void setBadgeDateCreated(long badgeDateCreated) {
        this.badgeDateCreated = badgeDateCreated;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
