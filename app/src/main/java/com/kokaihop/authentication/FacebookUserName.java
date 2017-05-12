package com.kokaihop.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 12/5/17.
 */

public class FacebookUserName {

    @SerializedName("first")
    private String firstName;

    @SerializedName("full")
    private String fullName;

    @SerializedName("last")
    private String lastName;

    public FacebookUserName(String firstName, String fullName, String lastName) {
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
    }
}