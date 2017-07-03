package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserNameRealmObject extends RealmObject{

    @SerializedName("first")
    private String first;

    @SerializedName("last")
    private String last;

    @PrimaryKey
    @SerializedName("full")
    private String full;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}
