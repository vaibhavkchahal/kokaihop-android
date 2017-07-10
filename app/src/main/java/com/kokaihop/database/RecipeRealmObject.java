package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class RecipeRealmObject extends RealmObject  {

    @PrimaryKey
    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    @SerializedName(value = "_id", alternate = {"id"})
    private String _id;
    @SerializedName("dateCreated")
    private String dateCreated;
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
    private String coverImage;

    @SerializedName(("categoryImagePublicId"))
    private String categoryImagePublicId;

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

    private RealmList<CookingStep> cookingSteps;

    public boolean isFavorite;

    @SerializedName("mainImage")
    private MainImageRealmObject mainImage;
    @SerializedName("rating")
    private RatingRealmObject rating;
    private long badgeDateCreated;

    private long lastUpdated;
    private long lastViewed;
    private boolean viewed;

    private RealmList<RecipeDetailPagerImages> images;

    private RealmList<RecipeRealmObject> similarRecipes;

    @SerializedName("comments")
    private RealmList<CommentRealmObject> comments;

    public MainImageRealmObject getMainImage() {
        return mainImage;
    }

    public RatingRealmObject getRating() {
        return rating;
    }

    public RealmList<RecipeDetailPagerImages> getImages() {
        return images;
    }

    public void setImages(RealmList<RecipeDetailPagerImages> images) {
        this.images = images;
    }

    public void setRating(RatingRealmObject rating) {
        this.rating = rating;
    }

    public void setMainImage(MainImageRealmObject mainImage) {
        this.mainImage = mainImage;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
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

    public RealmList<CookingStep> getCookingSteps() {
        return cookingSteps;
    }

    public void setCookingSteps(RealmList<CookingStep> cookingSteps) {
        this.cookingSteps = cookingSteps;
    }

    public String getCategoryImagePublicId() {
        return categoryImagePublicId;
    }

    public void setCategoryImagePublicId(String categoryImagePublicId) {
        this.categoryImagePublicId = categoryImagePublicId;
    }


}
