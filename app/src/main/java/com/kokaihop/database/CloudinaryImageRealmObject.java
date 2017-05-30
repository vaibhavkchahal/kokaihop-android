package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class CloudinaryImageRealmObject extends RealmObject {

    @SerializedName("cloudinaryId")
    private String cloudinaryId;

    @SerializedName("uploaded")
    private long uploaded;

    public String getCloudinaryId() {
        return cloudinaryId;
    }

    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    public long getUploaded() {
        return uploaded;
    }

    public void setUploaded(long uploaded) {
        this.uploaded = uploaded;
    }
}
