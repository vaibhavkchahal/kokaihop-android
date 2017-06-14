package com.kokaihop.editprofile.model;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.userprofile.model.CloudinaryImage;

/**
 * Created by Rajendra Singh on 13/6/17.
 */

public class ProfileImageUpdateRequest {

    @SerializedName("profileImage")
    private CloudinaryImage profileImage;

    public CloudinaryImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CloudinaryImage profileImage) {
        this.profileImage = profileImage;
    }
}
