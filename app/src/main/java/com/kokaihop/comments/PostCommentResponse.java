package com.kokaihop.comments;

import com.kokaihop.database.CommentRealmObject;

/**
 * Created by Vaibhav Chahal on 20/6/17.
 */
public class PostCommentResponse {


    private CommentRealmObject commentRealmObject;

    public CommentRealmObject getCommentRealmObject() {
        return commentRealmObject;
    }

    public void setCommentRealmObject(CommentRealmObject commentRealmObject) {
        this.commentRealmObject = commentRealmObject;
    }
}
