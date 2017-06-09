package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 5/6/17.
 */
public class RecipeDescription extends RealmObject {

    private String longDescription;

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
