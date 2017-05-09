package com.kokaihop.recipe;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class RecipeDetails extends RealmObject {

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
