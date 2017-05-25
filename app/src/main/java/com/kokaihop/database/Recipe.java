package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class Recipe extends RealmObject {

    @PrimaryKey
    @SerializedName(value = "_id", alternate = {"id"})
    private String _id;
    @SerializedName("dateCreated")
    private long dateCreated;
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String status;
    @SerializedName("cuisine")
    private Cuisine cuisine;
    @SerializedName("createdBy")
    private CreatedBy createdBy;
    @SerializedName("ingredients")
    private RealmList<Ingredients> ingredientsList;
    @SerializedName("category")
    private Category category;
    @SerializedName("cookingMethod")
    private CookingMethod cookingMethod;
    @SerializedName("counter")
    private Counter counter;
    @SerializedName("badgeType")
    private String badgeType;
    @SerializedName("isMine")
    private boolean isMine;

    private String cookingSteps;

    public boolean isFavorite;

    @SerializedName("mainImage")
    private MainImage mainImage;
    @SerializedName("rating")
    private Rating rating;
    private long badgeDateCreated;

    private long lastUpdated;
    private long lastViewed;
    private boolean viewed;

    public MainImage getMainImage() {
        return mainImage;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setMainImage(MainImage mainImage) {
        this.mainImage = mainImage;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public RealmList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(RealmList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CookingMethod getCookingMethod() {
        return cookingMethod;
    }

    public void setCookingMethod(CookingMethod cookingMethod) {
        this.cookingMethod = cookingMethod;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
    }

    public String getCookingSteps() {
        return cookingSteps;
    }

    public void setCookingSteps(String cookingSteps) {
        this.cookingSteps = cookingSteps;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public long getBadgeDateCreated() {
        return badgeDateCreated;
    }

    public void setBadgeDateCreated(long badgeDateCreated) {
        this.badgeDateCreated = badgeDateCreated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long isLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(long lastViewed) {
        this.lastViewed = lastViewed;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
