package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingFollowerUser{

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

    private int buttonVisibility = View.VISIBLE;

    private boolean followingUser = true;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}
