package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeCommentInfo extends RealmObject {
    private String id, oldId;

    public String getId() {
        return id;
    }

    public String getOldId() {
        return oldId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }
}
