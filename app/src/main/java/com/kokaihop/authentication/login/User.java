package com.kokaihop.authentication.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */
public class User {

    @SerializedName("id")
    private String id;

    @SerializedName("friendlyUrl")
    private String friendlyUrl;

    public String getId() {
        return id;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }
}
