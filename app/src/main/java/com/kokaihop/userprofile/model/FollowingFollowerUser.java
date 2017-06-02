package com.kokaihop.userprofile.model;

import android.view.View;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingFollowerUser {

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private UserName name;

    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    @SerializedName("aboutMe")
    private String aboutMe;

    @SerializedName("profileImage")
    private CloudinaryImage profileImage;

    private String profileImageUrl;

    private int buttonVisibility = View.VISIBLE;

    private boolean followingUser = true;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public CloudinaryImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CloudinaryImage profileImage) {
        this.profileImage = profileImage;
    }

    public int getButtonVisibility() {
        return buttonVisibility;
    }

    public void setButtonVisibility(int buttonVisibility) {
        this.buttonVisibility = buttonVisibility;
    }

    public boolean isFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(boolean followingUser) {
        this.followingUser = followingUser;
    }

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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
