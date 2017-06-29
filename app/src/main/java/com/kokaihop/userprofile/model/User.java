package com.kokaihop.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.feed.Recipe;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class User extends BaseObservable {

    private static User user;
    private static User otherUser;

    private String _id;
    private String friendlyUrl;
    private String email;
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();
    private ArrayList<Recipe> recipesList = new ArrayList<>();
    private int followersCount;
    private int followingCount;
    private Settings settings;
    private UserName name;
    private CloudinaryImage profileImage;
    private String profileImageUrl;
    private int recipesCollectionCount;
    private CloudinaryImage coverImage;
    private int recipeCount;
    private String cityName;
    private boolean followByMe;
    private ArrayList<Cookbook> cookbooks = new ArrayList<>();

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

    @Bindable
    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
        notifyPropertyChanged(BR.followers);
    }

    @Bindable
    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
        notifyPropertyChanged(BR.following);
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

    @Bindable
    public int getRecipesCollectionCount() {
        return recipesCollectionCount;
    }

    public void setRecipesCollectionCount(int recipesCollectionCount) {
        this.recipesCollectionCount = recipesCollectionCount;
        notifyPropertyChanged(BR.recipesCollectionCount);
    }

    public CloudinaryImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CloudinaryImage coverImage) {
        this.coverImage = coverImage;
    }

    @Bindable
    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
        notifyPropertyChanged(BR.recipeCount);
    }

    @Bindable
    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        notifyPropertyChanged(BR.profileImageUrl);
    }

    @Bindable
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
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

    public User() {
    }

    ;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static User getOtherUser() {
        if (otherUser == null) {
            otherUser = new User();
        }
        return otherUser;
    }

    public void removeOtherUser() {
        otherUser = null;
    }

    public static void removeInstance() {
        user = null;
    }

    public ArrayList<Recipe> getRecipesList() {
        return recipesList;
    }

    public void setRecipesList(ArrayList<Recipe> recipesList) {
        this.recipesList = recipesList;
    }

    @Bindable
    public boolean isFollowByMe() {
        return followByMe;
    }

    public void setFollowByMe(boolean followByMe) {
        this.followByMe = followByMe;
        notifyPropertyChanged(BR.followByMe);
    }

    public ArrayList<Cookbook> getCookbooks() {
        return cookbooks;
    }

    public void setCookbooks(ArrayList<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
    }
}