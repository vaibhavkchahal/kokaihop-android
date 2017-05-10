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
    @SerializedName("_id")
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

    @SerializedName("cookingMethod")

    private CookingMethod cookingMethod;
    @SerializedName("counter")
    private Counter counter;
    private String badgeType;
    private String cookingSteps;
    private boolean isFavorite;
    @SerializedName("isMine")
    private boolean isMine;


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


}
