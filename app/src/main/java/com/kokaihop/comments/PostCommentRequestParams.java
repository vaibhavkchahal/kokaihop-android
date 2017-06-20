package com.kokaihop.comments;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 20/6/17.
 */
public class PostCommentRequestParams {

    @SerializedName("comment")
    private String comment;

    @SerializedName("type")
    private String type;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("replyId")
    private String replyId;

    @SerializedName("taggedUsers")
    private String[] taggedUsers;


    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public void setTaggedUsers(String[] taggedUsers) {
        this.taggedUsers = taggedUsers;
    }
}
