package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingApiResponse {

    private static FollowingApiResponse followingApiResponse;

    @SerializedName("users")
    private ArrayList<FollowingUser> users;

    @SerializedName("total")
    private int total;

    private FollowingApiResponse(){
    }

    public static FollowingApiResponse getInstance(){
        if(followingApiResponse==null){
            followingApiResponse = new FollowingApiResponse();
        }
        return followingApiResponse;
    }

    public ArrayList<FollowingUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<FollowingUser> users) {
        this.users = users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
