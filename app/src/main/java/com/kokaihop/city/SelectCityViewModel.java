package com.kokaihop.city;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class SelectCityViewModel {
    ArrayList<String> cityList = new ArrayList<>();

    public SelectCityViewModel() {
        cityList.add("A");
    }

    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<String> cityList) {
        this.cityList = cityList;
    }
}
