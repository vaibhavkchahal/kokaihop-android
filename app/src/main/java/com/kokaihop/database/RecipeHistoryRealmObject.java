package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class RecipeHistoryRealmObject extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("timeStamp")
    private long timeStamp;

    public RecipeHistoryRealmObject() {
    }

    public RecipeHistoryRealmObject(String id, long timeStamp) {
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

