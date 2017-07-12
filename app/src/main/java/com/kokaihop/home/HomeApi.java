package com.kokaihop.home;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public interface HomeApi {

    @GET("v1/api/shoppingLists/show/{userId}")
    Call<ShoppingListResponse> getShoppingList(@Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("v1/api/units")
    Call<ResponseBody> getIngredientUnits(@Header("Authorization") String authorization);

}
