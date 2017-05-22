package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class UserApiResponse extends BaseObservable{

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
    private String[] followers;
    @SerializedName("following")
    private String[] following;
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

    @Bindable
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
        notifyPropertyChanged(BR._id);

    }

    @Bindable
    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
        notifyPropertyChanged(BR.friendlyUrl);
    }

    @Bindable
    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
        notifyPropertyChanged(BR.oldId);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    @Bindable
    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
        notifyPropertyChanged(BR.followers);
    }

    @Bindable
    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
        notifyPropertyChanged(BR.following);
    }

    @Bindable
    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
        notifyPropertyChanged(BR.dateCreated);
    }

    @Bindable
    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
        notifyPropertyChanged(BR.loginCount);
    }

    @Bindable
    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        notifyPropertyChanged(BR.lastLoginDate);

    }

    @Bindable
    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        notifyPropertyChanged(BR.settings);
    }

    @Bindable
    public long getLastViewedNewsOn() {
        return lastViewedNewsOn;
    }

    public void setLastViewedNewsOn(long lastViewedNewsOn) {
        this.lastViewedNewsOn = lastViewedNewsOn;
        notifyPropertyChanged(BR.lastViewedNewsOn);
    }

    @Bindable
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        notifyPropertyChanged(BR.role);

    }

    @Bindable
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyPropertyChanged(BR.enabled);
    }

    @Bindable
    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
        notifyPropertyChanged(BR.__v);
    }

    @Bindable
    public CloudinaryImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CloudinaryImage profileImage) {
        this.profileImage = profileImage;
        notifyPropertyChanged(BR.profileImage);
    }

    @Bindable
    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
        notifyPropertyChanged(BR.lastSeenTime);
    }

    @Bindable
    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
        notifyPropertyChanged(BR.online);
    }

    @Bindable
    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
        notifyPropertyChanged(BR.aboutMe);
    }

    @Bindable
    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
        notifyPropertyChanged(BR.counter);
    }

    @Bindable
    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
        notifyPropertyChanged(BR.hasPassword);
    }

    @Bindable
    public int getRecipesCollectionCount() {
        return recipesCollectionCount;
    }

    public void setRecipesCollectionCount(int recipesCollectionCount) {
        this.recipesCollectionCount = recipesCollectionCount;
        notifyPropertyChanged(BR.recipesCollectionCount);
    }

    @Bindable
    public int getBlogPostCount() {
        return blogPostCount;
    }

    public void setBlogPostCount(int blogPostCount) {
        this.blogPostCount = blogPostCount;
        notifyPropertyChanged(BR.blogPostCount);
    }

    @Bindable
    public CloudinaryImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CloudinaryImage coverImage) {
        this.coverImage = coverImage;
        notifyPropertyChanged(BR.coverImage);
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
    public int getTotalFeeds() {
        return totalFeeds;
    }

    public void setTotalFeeds(int totalFeeds) {
        this.totalFeeds = totalFeeds;
        notifyPropertyChanged(BR.totalFeeds);
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

