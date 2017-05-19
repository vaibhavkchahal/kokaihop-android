package com.kokaihop.home.userprofile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class User extends BaseObservable{
    private boolean following;
    private String name;
    private String avatarUrl;

    public User(boolean following, String name, String avatarUrl) {
        this.following = following;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    @Bindable
    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
        notifyPropertyChanged(BR.following);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
