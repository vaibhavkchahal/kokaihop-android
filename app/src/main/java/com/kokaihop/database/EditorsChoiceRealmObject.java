package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 9/8/17.
 */

public class EditorsChoiceRealmObject extends RealmObject {

    @PrimaryKey
    @SerializedName("_id")
    private String _id;

    @SerializedName("section")
    private String section;

    @SerializedName("categoryName")
    private CategoryName categoryName;

    @SerializedName("lastUpdated")
    private long lastUpdated;

    @SerializedName("payload")
    private RealmList<RecipeRealmObject> payload;

    @SerializedName("__v")
    private int __v;

    @SerializedName("lastUpdatedBy")
    private CreatedByRealmObject lastUpdatedBy;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public RealmList<RecipeRealmObject> getPayload() {
        return payload;
    }

    public void setPayload(RealmList<RecipeRealmObject> payload) {
        this.payload = payload;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public CreatedByRealmObject getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(CreatedByRealmObject lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
