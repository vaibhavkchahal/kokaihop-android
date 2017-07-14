package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingListRealmObject extends RealmObject {

    @PrimaryKey
    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    @SerializedName(value = "_id", alternate = {"id"})
    private String _id;
    @SerializedName("dateCreated")
    private String dateCreated;
    @SerializedName("oldId")
    private String oldId;
    @SerializedName(("name"))
    private String name;
    @SerializedName(("lastUpdated"))
    private String lastUpdated;
    @SerializedName(("__v"))
    private String __v;

    @SerializedName("items")
    private RealmList<IngredientsRealmObject> ingredients;

    @SerializedName("createdBy")
    private CreatedByRealmObject createdBy;

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
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

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public RealmList<IngredientsRealmObject> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<IngredientsRealmObject> ingredients) {
        this.ingredients = ingredients;
    }

    public CreatedByRealmObject getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByRealmObject createdBy) {
        this.createdBy = createdBy;
    }
}