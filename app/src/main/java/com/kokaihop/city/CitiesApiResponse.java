package com.kokaihop.city;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class CitiesApiResponse {
    @SerializedName("cities")
    ArrayList<CityDetails> cityDetailsList;

    public CitiesApiResponse(ArrayList<CityDetails> cityDetailsList) {
        this.cityDetailsList = cityDetailsList;
    }

    public ArrayList<CityDetails> getCityDetailsList() {
        return cityDetailsList;
    }

    public void setCityDetailsList(ArrayList<CityDetails> cityDetailsList) {
        this.cityDetailsList = cityDetailsList;
    }

    @Override
    public String toString() {
        return "GetCitiesResponse{" +
                "cityDetailsList=" + cityDetailsList +
                '}';
    }
}
