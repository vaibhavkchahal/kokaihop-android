package com.kokaihop.search;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchFilterApiHelper {


    public void fetchCategories(final IApiRequestComplete successCallback) {
        SearchFilterApi searchFilterApi = RetrofitClient.getInstance().create(SearchFilterApi.class);
        Call<ResponseBody> categories = searchFilterApi.recipeCategories();
        categories.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }

    public void fetchCuisines(final IApiRequestComplete successCallback) {
        SearchFilterApi searchFilterApi = RetrofitClient.getInstance().create(SearchFilterApi.class);
        Call<ResponseBody> categories = searchFilterApi.cuisines();
        categories.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }

    public void fetchCookingMethods(final IApiRequestComplete successCallback) {
        SearchFilterApi searchFilterApi = RetrofitClient.getInstance().create(SearchFilterApi.class);
        Call<ResponseBody> categories = searchFilterApi.cookingMethods();
        categories.enqueue(new ResponseHandler<ResponseBody>(successCallback));
    }
}
