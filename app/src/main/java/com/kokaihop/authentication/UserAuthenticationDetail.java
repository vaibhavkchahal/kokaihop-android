package com.kokaihop.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */
public class UserAuthenticationDetail {

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
