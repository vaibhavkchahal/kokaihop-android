package com.kokaihop.editprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 13/6/17.
 */

public class CityLocation {
    @SerializedName("living")
    private CityLiving living;

    public CityLiving getLiving() {
        return living;
    }

    public void setLiving(CityLiving living) {
        this.living = living;
    }
}
