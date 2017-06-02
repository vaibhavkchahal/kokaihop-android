package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class RecipeRealmObject extends RealmObject {

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
    private CuisineRealmObject cuisineRealmObject;
    @SerializedName("createdBy")
    private CreatedByRealmObject createdByRealmObject;
    @SerializedName("ingredients")
    private RealmList<IngredientsRealmObject> ingredientsRealmObjectList;
    @SerializedName("category")
    private CategoryRealmObject categoryRealmObject;
    @SerializedName("cookingMethod")
    private CookingMethodRealmObject cookingMethodRealmObject;
    @SerializedName("counter")
    private CounterRealmObject counterRealmObject;
    @SerializedName("badgeType")
    private String badgeType;
    @SerializedName("isMine")
    private boolean isMine;

    @SerializedName("servings")
    private int servings;

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

//    private StringObject[] cookingSteps;

    public boolean isFavorite;

    @SerializedName("mainImage")
    private MainImageRealmObject mainImageRealmObject;
    @SerializedName("rating")
    private RatingRealmObject ratingRealmObject;
    private long badgeDateCreated;

    private long lastUpdated;
    private long lastViewed;
    private boolean viewed;

    @SerializedName("comments")
    private RealmList<CommentRealmObject> comments;

    public MainImageRealmObject getMainImageRealmObject() {
        return mainImageRealmObject;
    }

    public RatingRealmObject getRatingRealmObject() {
        return ratingRealmObject;
    }

    public void setRatingRealmObject(RatingRealmObject ratingRealmObject) {
        this.ratingRealmObject = ratingRealmObject;
    }

    public void setMainImageRealmObject(MainImageRealmObject mainImageRealmObject) {
        this.mainImageRealmObject = mainImageRealmObject;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CuisineRealmObject getCuisineRealmObject() {
        return cuisineRealmObject;
    }

    public void setCuisineRealmObject(CuisineRealmObject cuisineRealmObject) {
        this.cuisineRealmObject = cuisineRealmObject;
    }

    public CreatedByRealmObject getCreatedByRealmObject() {
        return createdByRealmObject;
    }

    public void setCreatedByRealmObject(CreatedByRealmObject createdByRealmObject) {
        this.createdByRealmObject = createdByRealmObject;
    }

    public RealmList<IngredientsRealmObject> getIngredientsRealmObjectList() {
        return ingredientsRealmObjectList;
    }

    public void setIngredientsRealmObjectList(RealmList<IngredientsRealmObject> ingredientsRealmObjectList) {
        this.ingredientsRealmObjectList = ingredientsRealmObjectList;
    }

    public CategoryRealmObject getCategoryRealmObject() {
        return categoryRealmObject;
    }

    public void setCategoryRealmObject(CategoryRealmObject categoryRealmObject) {
        this.categoryRealmObject = categoryRealmObject;
    }

    public CookingMethodRealmObject getCookingMethodRealmObject() {
        return cookingMethodRealmObject;
    }

    public void setCookingMethodRealmObject(CookingMethodRealmObject cookingMethodRealmObject) {
        this.cookingMethodRealmObject = cookingMethodRealmObject;
    }

    public CounterRealmObject getCounterRealmObject() {
        return counterRealmObject;
    }

    public void setCounterRealmObject(CounterRealmObject counterRealmObject) {
        this.counterRealmObject = counterRealmObject;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
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

    public RealmList<CommentRealmObject> getComments() {
        return comments;
    }

    public void setComments(RealmList<CommentRealmObject> comments) {
        this.comments = comments;
    }

  /*  public StringObject[] getCookingSteps() {
        return cookingSteps;
    }

    public void setCookingSteps(StringObject[] cookingSteps) {
        this.cookingSteps = cookingSteps;
    }*/
}
