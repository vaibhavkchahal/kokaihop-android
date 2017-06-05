package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 5/6/17.
 */
public class ImageUploader extends RealmObject{

    private String id;
    private String name;
    private String friendlyUrl;
    private String oldId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }
}
