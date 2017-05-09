package com.kokaihop.authentication.signup;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.authentication.login.User;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */
public class SignUpApiResponse {

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
