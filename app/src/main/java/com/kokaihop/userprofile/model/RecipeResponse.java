package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.RecipeRealmObject;

import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 15/6/17.
 */

public class RecipeResponse {

    @SerializedName("recipes")
    private RealmList<RecipeRealmObject> recipes;
    @SerializedName("count")
    private int count;
    @SerializedName("myLikes")
    private Object[] myLikes;
    @SerializedName("myRatings")
    private Object myRatings;
    @SerializedName("collectionMappings")
    private Object collectionMappings;

    public RealmList<RecipeRealmObject> getRecipes() {
        return recipes;
    }

    public void setRecipes(RealmList<RecipeRealmObject> recipes) {
        this.recipes = recipes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object[] getMyLikes() {
        return myLikes;
    }

    public void setMyLikes(Object[] myLikes) {
        this.myLikes = myLikes;
    }

    public Object getMyRatings() {
        return myRatings;
    }

    public void setMyRatings(Object myRatings) {
        this.myRatings = myRatings;
    }

    public Object getCollectionMappings() {
        return collectionMappings;
    }

    public void setCollectionMappings(Object collectionMappings) {
        this.collectionMappings = collectionMappings;
    }
}
