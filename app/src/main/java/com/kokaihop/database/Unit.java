package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class Unit extends RealmObject {

    @SerializedName("name")
    private String name;

    @PrimaryKey
    @SerializedName(value = "_id", alternate = {"id"})
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
