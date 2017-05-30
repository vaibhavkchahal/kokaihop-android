package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class ToggleFollowingRequest {

    @SerializedName("friendId")
    private String friendId;

    @SerializedName("followRequest")
    private boolean followRequest;

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public boolean getFollowRequest() {
        return followRequest;
    }

    public void setFollowRequest(boolean followRequest) {
        this.followRequest = followRequest;
    }
}
