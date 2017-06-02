package com.kokaihop.database;


import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */
public class Payload extends RealmObject{

    private RecipeComment comment;

    public RecipeComment getComment() {
        return comment;
    }

    public void setComment(RecipeComment comment) {
        this.comment = comment;
    }

}
