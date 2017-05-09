package com.kokaihop.city;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public interface CitiesApiInterface {

    @GET("/v1/api/cities")
    Call<CitiesApiResponse> getCities();
}
