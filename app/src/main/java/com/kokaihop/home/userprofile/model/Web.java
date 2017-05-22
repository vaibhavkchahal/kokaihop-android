package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class Web {

    @SerializedName("profilePicture")
    private boolean profilePicture;

    @SerializedName("newRecipes")
    private boolean newRecipes;

    @SerializedName("newBlogs")
    private boolean newBlogs;

    public boolean isProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(boolean profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isNewRecipes() {
        return newRecipes;
    }

    public void setNewRecipes(boolean newRecipes) {
        this.newRecipes = newRecipes;
    }

    public boolean isNewBlogs() {
        return newBlogs;
    }

    public void setNewBlogs(boolean newBlogs) {
        this.newBlogs = newBlogs;
    }
}
