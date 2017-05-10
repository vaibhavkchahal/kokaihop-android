package com.kokaihop.city;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.authentication.signup.SignUpSettings;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class SignUpRequest {

    @SerializedName("location")
    private CityLocation cityLocation;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("settings")
    private SignUpSettings signUpSettings;

    public SignUpRequest(CityLocation cityLocation, String email, String name, String password, SignUpSettings signUpSettings) {
        this.cityLocation = cityLocation;
        this.email = email;
        this.name = name;
        this.password = password;
        this.signUpSettings = signUpSettings;
    }
}
