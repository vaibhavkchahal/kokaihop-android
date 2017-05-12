package com.kokaihop.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 12/5/17.
 */

public class FacebookUserLocation {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public FacebookUserLocation(String id, String name) {
        this.id = id;
        this.name = name;
    }
}