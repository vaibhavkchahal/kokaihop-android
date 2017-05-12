package com.kokaihop.authentication.login;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.authentication.User;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */
public class LoginApiResponse{

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
