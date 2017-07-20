package com.kokaihop.city;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Rajendra Singh on 18/7/17.
 */

public class CityApiHelper {
    private CitiesApi citiesApi;

    public CityApiHelper() {
        citiesApi = RetrofitClient.getInstance().create(CitiesApi.class);
    }

    public void getCities(final IApiRequestComplete successInterface) {
        Call<CitiesApiResponse> recipeOfCookbook = citiesApi.getCities();
        recipeOfCookbook.enqueue(new ResponseHandler<CitiesApiResponse>(successInterface));
    }
}