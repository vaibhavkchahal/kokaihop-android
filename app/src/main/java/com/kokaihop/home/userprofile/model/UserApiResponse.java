package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class UserApiResponse{

    private static UserApiResponse userApiResponse;


    @SerializedName("_id")
    private String _id;
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("oldId")
    private int oldId;
    @SerializedName("email")
    private String email;
    @SerializedName("location")
    private Location location;
    @SerializedName("followers")
    private ArrayList<String> followers;
    @SerializedName("following")
    private ArrayList<String> following;
    @SerializedName("dateCreated")
    private long dateCreated;
    @SerializedName("loginCount")
    private int loginCount;
    @SerializedName("lastLoginDate")
    private long lastLoginDate;
    @SerializedName("settings")
    private Settings settings;
    @SerializedName("lastViewedNewsOn")
    private long lastViewedNewsOn;
    @SerializedName("role")
    private String role;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("name")
    private UserName name;
    @SerializedName("__v")
    private int __v;
    @SerializedName("profileImage")
    private CloudinaryImage profileImage;
    @SerializedName("lastSeenTime")
    private long lastSeenTime;
    @SerializedName("isOnline")
    private boolean isOnline;
    @SerializedName("aboutMe")
    private String aboutMe;
    @SerializedName("counter")
    private Counter counter;
    @SerializedName("hasPassword")
    private boolean hasPassword;
    @SerializedName("recipesCollectionCount")
    private int recipesCollectionCount;
    @SerializedName("blogPostCount")
    private int blogPostCount;
    @SerializedName("coverImage")
    private CloudinaryImage coverImage;
    @SerializedName("recipeCount")
    private int recipeCount;
    @SerializedName("totalFeeds")
    private int totalFeeds;

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

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setLastViewedNewsOn(long lastViewedNewsOn) {
        this.lastViewedNewsOn = lastViewedNewsOn;
    }

    public long getLastViewedNewsOn() {
        return lastViewedNewsOn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public CloudinaryImage getProfileImage() {
        return profileImage;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public void setProfileImage(CloudinaryImage profileImage) {
        this.profileImage = profileImage;
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public int getRecipesCollectionCount() {
        return recipesCollectionCount;
    }

    public void setRecipesCollectionCount(int recipesCollectionCount) {
        this.recipesCollectionCount = recipesCollectionCount;
    }

    public int getBlogPostCount() {
        return blogPostCount;
    }

    public void setBlogPostCount(int blogPostCount) {
        this.blogPostCount = blogPostCount;
    }

    public CloudinaryImage getCoverImage() {
        return coverImage;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setCoverImage(CloudinaryImage coverImage) {
        this.coverImage = coverImage;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }

    public int getTotalFeeds() {
        return totalFeeds;
    }

    public void setTotalFeeds(int totalFeeds) {
        this.totalFeeds = totalFeeds;
    }

    private UserApiResponse(){};

    public static UserApiResponse getUserApiResponse() {
        return userApiResponse;
    }

    public static void setUserApiResponse(UserApiResponse userApiResponse) {
        UserApiResponse.userApiResponse = userApiResponse;
    }

    public static UserApiResponse getInstance()
    {
        if(userApiResponse == null){
            userApiResponse = new UserApiResponse();
        }
        return userApiResponse;
    }

}

