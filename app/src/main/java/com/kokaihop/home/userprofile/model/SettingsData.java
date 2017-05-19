package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

class SettingsData extends BaseObservable{

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;
}
