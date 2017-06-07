package com.kokaihop.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class User extends BaseObservable{

    private static User user;

    private String _id;
    private String friendlyUrl;
    private String email;
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();
    private int followersCount;
    private int followingCount;
    private Settings settings;
    private UserName name;
    private CloudinaryImage profileImage;
    private String profileImageUrl;
    private int recipesCollectionCount;
    private CloudinaryImage coverImage;
    private int recipeCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
    }

    public CloudinaryImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CloudinaryImage profileImage) {
        this.profileImage = profileImage;
    }

    public int getRecipesCollectionCount() {
        return recipesCollectionCount;
    }

    public void setRecipesCollectionCount(int recipesCollectionCount) {
        this.recipesCollectionCount = recipesCollectionCount;
    }

    public CloudinaryImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CloudinaryImage coverImage) {
        this.coverImage = coverImage;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }


    @Bindable
    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
        notifyPropertyChanged(BR.followersCount);
    }

    @Bindable
    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
        notifyPropertyChanged(BR.followingCount);
    }

    private User(){};

    public static User getInstance()
    {
        if(user == null){
            user = new User();
        }
        return user;
    }
}