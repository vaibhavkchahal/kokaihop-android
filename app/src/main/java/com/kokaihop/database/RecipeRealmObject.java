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
    private CuisineRealmObject cuisine;
    @SerializedName("createdBy")
    private CreatedByRealmObject createdBy;
    @SerializedName("ingredients")
    private RealmList<IngredientsRealmObject> ingredients;
    @SerializedName("category")
    private CategoryRealmObject category;
    @SerializedName("cookingMethod")
    private CookingMethod cookingMethod;
    @SerializedName("counter")
    private CounterRealmObject counter;
    @SerializedName("badgeType")
    private String badgeType;
    @SerializedName("isMine")
    private boolean isMine;

    @SerializedName("description")
    private RecipeDescription description;

    @SerializedName("servings")
    private int servings;

    @SerializedName("coverImage")
    private  String coverImage;

    public RecipeDescription getDescription() {
        return description;
    }

    public void setDescription(RecipeDescription description) {
        this.description = description;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

//    private RealmList<StringObject> cookingSteps;

    public boolean isFavorite;

    @SerializedName("mainImage")
    private MainImageRealmObject mainImageRealmObject;
    @SerializedName("rating")
    private RatingRealmObject ratingRealmObject;
    private long badgeDateCreated;

    private long lastUpdated;
    private long lastViewed;
    private boolean viewed;

    private RealmList<RecipeDetailPagerImages> images;

    private RealmList<RecipeRealmObject> similarRecipes;

    @SerializedName("comments")
    private RealmList<CommentRealmObject> comments;

    public MainImageRealmObject getMainImageRealmObject() {
        return mainImageRealmObject;
    }

    public RatingRealmObject getRatingRealmObject() {
        return ratingRealmObject;
    }

    public RealmList<RecipeDetailPagerImages> getImages() {
        return images;
    }

    public void setImages(RealmList<RecipeDetailPagerImages> images) {
        this.images = images;
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

    public RealmList<RecipeRealmObject> getSimilarRecipes() {
        return similarRecipes;
    }

    public void setSimilarRecipes(RealmList<RecipeRealmObject> similarRecipes) {
        this.similarRecipes = similarRecipes;
    }

    public CookingMethod getCookingMethod() {
        return cookingMethod;
    }

    public void setCookingMethod(CookingMethod cookingMethod) {
        this.cookingMethod = cookingMethod;
    }

    public CuisineRealmObject getCuisine() {
        return cuisine;
    }

    public void setCuisine(CuisineRealmObject cuisine) {
        this.cuisine = cuisine;
    }

    public CreatedByRealmObject getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByRealmObject createdBy) {
        this.createdBy = createdBy;
    }

    public RealmList<IngredientsRealmObject> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<IngredientsRealmObject> ingredients) {
        this.ingredients = ingredients;
    }

    public CategoryRealmObject getCategory() {
        return category;
    }

    public void setCategory(CategoryRealmObject category) {
        this.category = category;
    }

    public CounterRealmObject getCounter() {
        return counter;
    }

    public void setCounter(CounterRealmObject counter) {
        this.counter = counter;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

//    public RealmList<StringObject> getCookingSteps() {
//        return cookingSteps;
//    }
//
//    public void setCookingSteps(RealmList<StringObject> cookingSteps) {
//        this.cookingSteps = cookingSteps;
//    }
}
