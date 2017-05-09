package com.kokaihop.city;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class SelectCityViewModel implements SetCitiesInterface{
    private ArrayList<CityDetails> cityList = new ArrayList<>();
    private SelectCityInterface selectCityInterface;
    public SelectCityViewModel(SelectCityInterface selectCityInterface) {
        new CitiesHelper(this).getCities();
        this.selectCityInterface = selectCityInterface;
    }

    public ArrayList<CityDetails> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
    }

    @Override
    public void setCities(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
        selectCityInterface.setCitiesOnRecyclerView();
    }
}
