package com.kokaihop.city;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class CityViewModel extends BaseViewModel{
    private ArrayList<CityDetails> cityList = new ArrayList<>();
    private CityInterface cityInterface;

    public CityViewModel(CityInterface cityInterface) {
        getCities();
        this.cityInterface = cityInterface;
    }

    public ArrayList<CityDetails> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
    }

    public void setCities(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
        cityInterface.setCitiesOnRecyclerView();
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
                        Logger.e("Response",response.body().getCityDetailsList().get(0).getName());
                        setCities(response.body().getCityDetailsList());
                    }
                }else{
                    Logger.e("Response ", "Error");
                }
            }

            @Override
            public void onFailure(Call<CitiesApiResponse> call, Throwable t) {
                setProgressVisible(false);
            }
        });
    }

    @Override
    protected void destroy() {

    }

    public interface CityInterface {
        void citySelected(CityDetails selectedCity);
        void setCitiesOnRecyclerView();
    }

}

