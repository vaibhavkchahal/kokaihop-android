package com.kokaihop.editprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 13/6/17.
 */

public class CityUpdateRequest {

    @SerializedName("location")
    CityLocation location;

    public CityLocation getLocation() {
        return location;
    }

    public void setLocation(CityLocation location) {
        this.location = location;
    }
}
