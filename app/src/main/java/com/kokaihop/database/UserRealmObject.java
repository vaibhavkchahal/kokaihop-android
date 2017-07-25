package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class UserRealmObject extends RealmObject {

    @SerializedName(value = "_id", alternate = {"id"})
    private String _id;

    @PrimaryKey
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("oldId")
    private int oldId;
    @SerializedName("email")
    private String email;

    @SerializedName("followers")
    private RealmList<RealmString> followers;

    @SerializedName("following")
    private RealmList<RealmString> following;

    private RealmList<UserRealmObject> followersList;

    private RealmList<UserRealmObject> followingList;

    private RealmList<RecipeRealmObject> recipeList;

    @SerializedName("dateCreated")
    private String dateCreated;
    @SerializedName("loginCount")
    private int loginCount;
    @SerializedName("lastLoginDate")
    private long lastLoginDate;
    @SerializedName("settings")
    private SettingsRealmObject settings;
    @SerializedName("lastViewedNewsOn")
    private long lastViewedNewsOn;
    @SerializedName("role")
    private String role;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("name")
    private UserNameRealmObject userNameRealmObject;
    @SerializedName("profileImage")
    private CloudinaryImageRealmObject profileImage;
    @SerializedName("lastSeenTime")
    private long lastSeenTime;
    @SerializedName("isOnline")
    private boolean isOnline;
    @SerializedName("aboutMe")
    private String aboutMe;
    @SerializedName("hasPassword")
    private boolean hasPassword;
    @SerializedName("cityName")
    private String cityName;
    @SerializedName("recipesCollectionCount")
    private int recipesCollectionCount;
    @SerializedName("blogPostCount")
    private int blogPostCount;
    @SerializedName("coverImage")
    private CloudinaryImageRealmObject coverImage;
    @SerializedName("recipeCount")
    private int recipeCount;
    @SerializedName("totalFeeds")
    private int totalFeeds;
    @SerializedName("recipeCollections")
    private RealmList<CookbookRealmObject> recipeCollections;
    @SerializedName("userName")
    private String userName;

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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
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

    public SettingsRealmObject getSettings() {
        return settings;
    }

    public void setSettings(SettingsRealmObject settings) {
        this.settings = settings;
    }

    public long getLastViewedNewsOn() {
        return lastViewedNewsOn;
    }

    public void setLastViewedNewsOn(long lastViewedNewsOn) {
        this.lastViewedNewsOn = lastViewedNewsOn;
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


    public CloudinaryImageRealmObject getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CloudinaryImageRealmObject profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
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

    public CloudinaryImageRealmObject getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CloudinaryImageRealmObject coverImage) {
        this.coverImage = coverImage;
    }

    public int getRecipeCount() {
        return recipeCount;
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

    public RealmList<UserRealmObject> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(RealmList<UserRealmObject> followersList) {
        this.followersList = followersList;
    }

    public RealmList<UserRealmObject> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(RealmList<UserRealmObject> followingList) {
        this.followingList = followingList;
    }

    public RealmList<RealmString> getFollowers() {
        return followers;
    }

    public void setFollowers(RealmList<RealmString> followers) {
        this.followers = followers;
    }

    public RealmList<RealmString> getFollowing() {
        return following;
    }

    public void setFollowing(RealmList<RealmString> following) {
        this.following = following;
    }

    public String getId() {
        return _id;
    }


    public void setId(String _id) {
        this._id = _id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public UserNameRealmObject getUserNameRealmObject() {
        return userNameRealmObject;
    }

    public void setUserNameRealmObject(UserNameRealmObject userNameRealmObject) {
        this.userNameRealmObject = userNameRealmObject;
    }


    public String getUserName() {
        return userName;
    }

    public RealmList<RecipeRealmObject> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(RealmList<RecipeRealmObject> recipeList) {
        this.recipeList = recipeList;
    }

    public RealmList<CookbookRealmObject> getRecipeCollections() {
        return recipeCollections;
    }

    public void setRecipeCollections(RealmList<CookbookRealmObject> recipeCollections) {
        this.recipeCollections = recipeCollections;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

