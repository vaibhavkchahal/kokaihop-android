package com.kokaihop.city;

import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class SelectCityViewModel extends BaseViewModel implements SetCitiesInterface{
    private ArrayList<CityDetails> cityList = new ArrayList<>();
    private SelectCityInterface selectCityInterface;

    public SelectCityViewModel(SelectCityInterface selectCityInterface) {
        getCities();
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
        //Select City
    }

    public void getCities(){
        setProgressVisible(true);
        CitiesApi citiesApi = RetrofitClient.getInstance().create(CitiesApi.class);
        Call<CitiesApiResponse> getCitiesResponseCall = citiesApi.getCities();
        getCitiesResponseCall.enqueue(new Callback<CitiesApiResponse>() {
            @Override
            public void onResponse(Call<CitiesApiResponse> call, Response<CitiesApiResponse> response) {
                setProgressVisible(false);
                if(response!=null){
                    if(response.code()==200){
                        Log.e("Response",response.body().getCityDetailsList().get(0).getName());
                        setCities(response.body().getCityDetailsList());
                    }
                }else{
                    Log.e("Response ", "Error");
                }
            }

            @Override
            public void onFailure(Call<CitiesApiResponse> call, Throwable t) {
                setProgressVisible(false);
            }
        });
    }
}
