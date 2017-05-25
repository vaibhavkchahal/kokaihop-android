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

    public FacebookUserLocation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}