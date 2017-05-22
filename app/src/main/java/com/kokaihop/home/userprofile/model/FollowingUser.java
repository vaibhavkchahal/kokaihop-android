package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingUser {

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private UserName name;

    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    @SerializedName("aboutMe")
    private String aboutMe;

    @SerializedName("location")
    private Location location ;

    @SerializedName("profileImage")
    private CloudinaryImage profileImage;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }
}
