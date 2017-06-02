package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeComment extends RealmObject {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
