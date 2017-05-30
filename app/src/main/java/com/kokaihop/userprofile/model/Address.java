package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Address{

    @SerializedName("loc")
    private Loc loc;
    @SerializedName("geoId")
    private int geoId;
    @SerializedName("name")
    private String name;
    @SerializedName("countryCode")
    private String countryCode;

    public int getGeoId() {
        return geoId;
    }

    public void setGeoId(int geoId) {
        this.geoId = geoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }
}
