package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.UserRealmObject;

import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingFollowersApiResponse{

    @SerializedName("users")
    private RealmList<UserRealmObject> users = new RealmList<>();

    @SerializedName("total")
    private int total;

    public RealmList<UserRealmObject> getUsers() {
        return users;
    }

    public void setUsers(RealmList<UserRealmObject> users) {
        this.users = users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
