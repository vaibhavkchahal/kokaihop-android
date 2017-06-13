package com.kokaihop.city;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class CityLocation {

    @SerializedName("living")
    private CityDetails cityDetails;

    public CityLocation(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    @Override
    public String toString() {
        return "CityLocation{" +
                "cityDetails=" + cityDetails +
                '}';
    }
}
