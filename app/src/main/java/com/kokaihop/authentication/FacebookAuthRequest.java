package com.kokaihop.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 12/5/17.
 */

public class FacebookAuthRequest {

    @SerializedName("name")
    private FacebookUserName name;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("email")
    private String email;

    @SerializedName("fbId")
    private String fbId;

    @SerializedName("location")
    private FacebookUserLocation facebookLocation;


    public FacebookAuthRequest(FacebookUserName name, String accessToken, String email, String fbId, FacebookUserLocation facebookLocation) {
        this.name = name;
        this.accessToken = accessToken;
        this.email = email;
        this.fbId = fbId;
        this.facebookLocation = facebookLocation;
    }
}
