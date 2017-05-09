package com.kokaihop.city;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class SignUpCityLocation {

    @SerializedName("location")
    private CityLocation cityLocation;

    public SignUpCityLocation(CityLocation cityLocation) {
        this.cityLocation = cityLocation;
    }

    public CityLocation getCityLocation() {
        return cityLocation;
    }
}
