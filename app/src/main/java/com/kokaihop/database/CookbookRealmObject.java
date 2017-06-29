package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class CookbookRealmObject extends RealmObject {


    @PrimaryKey
    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    @SerializedName("recipes")
    private RealmList<RecipeRealmObject> recipes;

    @SerializedName("totalCount")
    private int totalCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public RealmList<RecipeRealmObject> getRecipes() {
        return recipes;
    }

    public void setRecipes(RealmList<RecipeRealmObject> recipes) {
        this.recipes = recipes;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
