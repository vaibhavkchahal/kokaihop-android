package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingFollowersApiResponse {

    private static FollowingFollowersApiResponse followingApiResponse;

    private static FollowingFollowersApiResponse followersApiResponse;

    @SerializedName("users")
    private ArrayList<FollowingFollowerUser> users;

    @SerializedName("total")
    private int total;

    private FollowingFollowersApiResponse(){
    }

    public static FollowingFollowersApiResponse getFollowingInstance(){
        if(followingApiResponse ==null){
            followingApiResponse = new FollowingFollowersApiResponse();
        }
        return followingApiResponse;
    }

    public static FollowingFollowersApiResponse getFollowersInstance(){
        if(followersApiResponse ==null){
            followersApiResponse = new FollowingFollowersApiResponse();
        }
        return followersApiResponse;
    }

    public ArrayList<FollowingFollowerUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<FollowingFollowerUser> users) {
        this.users = users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public static FollowingFollowersApiResponse getFollowingApiResponse() {
        return followingApiResponse;
    }

    public static void setFollowingApiResponse(FollowingFollowersApiResponse followingApiResponse) {
        FollowingFollowersApiResponse.followingApiResponse = followingApiResponse;
    }

    public static FollowingFollowersApiResponse getFollowersApiResponse() {
        return followersApiResponse;
    }

    public static void setFollowersApiResponse(FollowingFollowersApiResponse followersApiResponse) {
        FollowingFollowersApiResponse.followersApiResponse = followersApiResponse;
    }
}
