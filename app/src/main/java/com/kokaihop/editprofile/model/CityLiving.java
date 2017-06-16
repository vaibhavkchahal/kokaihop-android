package com.kokaihop.editprofile.model;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.city.Loc;

/**
 * Created by Rajendra Singh on 13/6/17.
 */

public class CityLiving {
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("geoId")
    private int geoId;
    @SerializedName("loc")
    private Loc loc;
    @SerializedName("name")
    private String name;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getGeoId() {
        return geoId;
    }

    public void setGeoId(int geoId) {
        this.geoId = geoId;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
