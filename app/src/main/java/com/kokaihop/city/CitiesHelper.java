package com.kokaihop.city;

import android.util.Log;

import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class CitiesHelper {
    final SetCitiesInterface setCitiesInterface;

    public CitiesHelper(SetCitiesInterface setCitiesInterface) {
        this.setCitiesInterface = setCitiesInterface;
    }

    public void getCities(){
        CitiesApiInterface citiesApiInterface = RetrofitClient.getInstance().create(CitiesApiInterface.class);
        Call<CitiesApiResponse> getCitiesResponseCall = citiesApiInterface.getCities();
        getCitiesResponseCall.enqueue(new Callback<CitiesApiResponse>() {
            @Override
            public void onResponse(Call<CitiesApiResponse> call, Response<CitiesApiResponse> response) {
                if(response!=null){
                    if(response.code()==200){
                        Log.e("Response",response.body().getCityDetailsList().get(0).getName());
                        setCitiesInterface.setCities(response.body().getCityDetailsList());
                    }
                }else{
                    Log.e("Response ", "Error");
                }
            }

            @Override
            public void onFailure(Call<CitiesApiResponse> call, Throwable t) {

            }
        });
    }
}
