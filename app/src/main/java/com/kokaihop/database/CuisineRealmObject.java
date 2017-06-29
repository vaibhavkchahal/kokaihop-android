package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 10/5/17.
 */

public class CuisineRealmObject extends RealmObject {

    @SerializedName("id")
    private String id;
    @SerializedName("oldId")
    private String oldId;
    @SerializedName("name")
    private String name;

    @SerializedName("friendlyUrl")
    @PrimaryKey
    private String friendlyUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public String getFriendlyUrl() {
        return friendlyUrl;
    }
}
