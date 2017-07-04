package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 18/5/17.
 */

public class MainImageRealmObject extends RealmObject {

    public String getPublicId() {
        return publicId;
    }

    @SerializedName("publicId")
    private String publicId;

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
