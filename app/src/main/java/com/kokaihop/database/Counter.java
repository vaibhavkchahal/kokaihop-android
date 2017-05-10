package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 10/5/17.
 */

public class Counter extends RealmObject {

    public long getAddedToCollection() {
        return addedToCollection;
    }

    public void setAddedToCollection(long addedToCollection) {
        this.addedToCollection = addedToCollection;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getPrinted() {
        return printed;
    }

    public void setPrinted(long printed) {
        this.printed = printed;
    }

    public long getMail() {
        return mail;
    }

    public void setMail(long mail) {
        this.mail = mail;
    }

    public long getViewed() {
        return viewed;
    }

    public void setViewed(long viewed) {
        this.viewed = viewed;
    }

    @SerializedName("addedToCollection")
    private long addedToCollection;
    @SerializedName("likes")
    private long likes;
    @SerializedName("comments")
    private long comments;
    @SerializedName("printed")
    private long printed;
    @SerializedName("mail")
    private long mail;
    @SerializedName("viewed")
    private long viewed;
}
