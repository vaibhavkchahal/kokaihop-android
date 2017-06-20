package com.kokaihop.search;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchViewModel extends BaseViewModel {
    SearchDataManager searchDataManager;


    public SearchViewModel() {
        fetchCategories();
        fetchCookingMethods();
        fetchCuisine();
        searchDataManager = new SearchDataManager();
    }


    public void fetchCategories() {
        new SearchFilterApiHelper().fetchCategories(new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    final JSONObject json = new JSONObject(responseBody.string());
                    searchDataManager.insertOrUpdateRecipeCategories(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.d("responseBody", responseBody.toString());

            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onError(Object response) {

            }
        });
    }

    public void fetchCuisine() {
        new SearchFilterApiHelper().fetchCuisines(new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    final JSONObject json = new JSONObject(responseBody.string());
                    searchDataManager.insertOrUpdateRecipeCuisine(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.d("responseBody", responseBody.toString());

            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onError(Object response) {

            }
        });
    }

    public void fetchCookingMethods() {
        new SearchFilterApiHelper().fetchCookingMethods(new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    final JSONObject json = new JSONObject(responseBody.string());
                    searchDataManager.insertOrUpdateRecipeCookingMethods(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.d("responseBody", responseBody.toString());

            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onError(Object response) {

            }
        });
    }


    @Override
    protected void destroy() {

    }
}
