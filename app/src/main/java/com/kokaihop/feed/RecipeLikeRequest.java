package com.kokaihop.feed;


import com.google.gson.annotations.SerializedName;

public class RecipeLikeRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("like")
    private boolean like;

    public RecipeLikeRequest(String id, boolean like) {
        this.id = id;
        this.like = like;
    }
}
