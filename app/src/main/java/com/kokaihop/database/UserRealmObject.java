package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class UserRealmObject extends RealmObject {

   @PrimaryKey @SerializedName(value = "_id", alternate = {"id"})
    private String _id;
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("oldId")
    private int oldId;
    @SerializedName("email")
    private String email;

    @SerializedName("followers")
    private RealmList<StringObject> followers;

    @SerializedName("following")
    private RealmList<StringObject> following;

    private RealmList<UserRealmObject> followersList;

    private RealmList<UserRealmObject> followingList;

    @SerializedName("dateCreated")
    private long dateCreated;
    @SerializedName("loginCount")
    private int loginCount;
    @SerializedName("lastLoginDate")
    private long lastLoginDate;
    @SerializedName("settings")
    private SettingsRealmObject settingsRealmObject;
    @SerializedName("lastViewedNewsOn")
    private long lastViewedNewsOn;
    @SerializedName("role")
    private String role;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("name")
    private UserNameRealmObject name;
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

    public SettingsRealmObject getSettingsRealmObject() {
        return settingsRealmObject;
    }

    public void setSettingsRealmObject(SettingsRealmObject settingsRealmObject) {
        this.settingsRealmObject = settingsRealmObject;
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

    public UserNameRealmObject getName() {
        return name;
    }

    public void setName(UserNameRealmObject name) {
        this.name = name;
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

    public RealmList<StringObject> getFollowers() {
        return followers;
    }

    public void setFollowers(RealmList<StringObject> followers) {
        this.followers = followers;
    }

    public RealmList<StringObject> getFollowing() {
        return following;
    }

    public void setFollowing(RealmList<StringObject> following) {
        this.following = following;
    }
}
