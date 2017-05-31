package com.kokaihop.userprofile.model;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 31/5/17.
 */

public class FollowersFollowingList {
    private ArrayList<FollowingFollowerUser> users = new ArrayList<>();

    private int total;

    private static FollowersFollowingList followingList;
    private static FollowersFollowingList followersList;

    private FollowersFollowingList() {
    }

    public static FollowersFollowingList getFollowingList(){
        if(followingList==null){
            followingList = new FollowersFollowingList();
        }
        return followingList;
    }
    public static FollowersFollowingList getFollowersList(){
        if(followersList==null){
            followersList = new FollowersFollowingList();
        }
        return followersList;
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
}
