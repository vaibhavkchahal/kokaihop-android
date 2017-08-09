package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 9/8/17.
 */

public class EditorsChoiceRealmObject extends RealmObject {

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

    @SerializedName("myLikes")
    private RealmList<RealmString> myLikes;

    @SerializedName("myRatings")
    private RealmObject myRatings;

    @SerializedName("collectionMapping")
    private RealmObject collectionMapping;

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

    public RealmList<RealmString> getMyLikes() {
        return myLikes;
    }

    public void setMyLikes(RealmList<RealmString> myLikes) {
        this.myLikes = myLikes;
    }

    public RealmObject getMyRatings() {
        return myRatings;
    }

    public void setMyRatings(RealmObject myRatings) {
        this.myRatings = myRatings;
    }

    public RealmObject getCollectionMapping() {
        return collectionMapping;
    }

    public void setCollectionMapping(RealmObject collectionMapping) {
        this.collectionMapping = collectionMapping;
    }

    private class CategoryName extends RealmObject {

        @SerializedName("en")
        private String en;

        @SerializedName("sv")
        private String sv;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getSv() {
            return sv;
        }

        public void setSv(String sv) {
            this.sv = sv;
        }
    }
}
