package com.kokaihop.home;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public class HomeApiHelper {
    private HomeApi homeApi;

    public HomeApiHelper() {
        this.homeApi = RetrofitClient.getInstance().create(HomeApi.class);
    }

    public void getShoppingList(String token, String userId, final IApiRequestComplete successInterface) {
        Call<ShoppingListResponse> recipeResponseCall = homeApi.getShoppingList(token, userId);
        recipeResponseCall.enqueue(new ResponseHandler<ShoppingListResponse>(successInterface));
    }

}
