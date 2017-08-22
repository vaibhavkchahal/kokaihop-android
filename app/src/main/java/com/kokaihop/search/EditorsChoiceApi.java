package com.kokaihop.search;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 9/8/17.
 */

public interface EditorsChoiceApi {

    @GET("v1/api/homePage/list")
    Call<ResponseBody> getEditorsChoice(@Query("section") String section,
                                        @Query("maxLength") int maxLength,
                                        @Query("type") String type);
}
