package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 1/6/17.
 */


public class CommentRealmObject extends RealmObject {

    @PrimaryKey @SerializedName("_id")
    private String _id;
    @SerializedName("name")
    private String name;
    @SerializedName("sourceUser")
    private UserRealmObject sourceUser;

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

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

    public UserRealmObject getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(UserRealmObject sourceUser) {
        this.sourceUser = sourceUser;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    @SerializedName("dateCreated")
    private  long dateCreated;

}
