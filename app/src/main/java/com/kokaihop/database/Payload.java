package com.kokaihop.database;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */
public class Payload extends RealmObject {

    private RecipeComment comment;

    private RecipeRealmObject recipe;

    private String replyId;

    private RealmList<CommentRealmObject> replyEvents;

    private int replyCount;

    public RecipeComment getComment() {
        return comment;
    }

    public void setComment(RecipeComment comment) {
        this.comment = comment;
    }

    public RecipeRealmObject getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeRealmObject recipe) {
        this.recipe = recipe;
    }

    public RealmList<CommentRealmObject> getReplyEvents() {
        return replyEvents;
    }

    public void setReplyEvents(RealmList<CommentRealmObject> replyEvents) {
        this.replyEvents = replyEvents;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }
}
