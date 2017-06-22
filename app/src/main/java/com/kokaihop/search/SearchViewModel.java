package com.kokaihop.search;

import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchViewModel extends BaseViewModel {
    private final DataSetListener dataSetListener;
    private SearchDataManager searchDataManager;
    private List<FilterData> categoriesList;
    private List<FilterData> cuisineList;
    private List<FilterData> cookingMethodList;


    public SearchViewModel(DataSetListener dataSetListener) {
        this.dataSetListener = dataSetListener;
        searchDataManager = new SearchDataManager();
        fetchCategories();
        fetchCookingMethods();
        fetchCuisine();
        dataSetListener.updateSearchSuggestions(getSearchSuggestion());
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

    public void displayCategoriesList(TextView textView) {

        if (categoriesList == null) {
            categoriesList = new ArrayList<>();
            FilterData filterDataAll = new FilterData();
            filterDataAll.setName(textView.getContext().getString(R.string.all));
            categoriesList.add(filterDataAll);

            if (searchDataManager.getCategories() != null) {
                for (CategoryRealmObject categoryRealmObject : searchDataManager.getCategories()
                        ) {
                    FilterData filterData = new FilterData();
                    filterData.setName(categoryRealmObject.getName());
                    filterData.setFriendlyUrl(categoryRealmObject.getFriendlyUrl());
                    categoriesList.add(filterData);
                }
            }
        } else {
            fetchCategories();
        }
        dataSetListener.showFilterDialog(categoriesList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_course));
    }

    public void displayCuisineList(TextView textView) {

        if (cuisineList == null) {
            cuisineList = new ArrayList<>();
            FilterData filterDataAll = new FilterData();
            filterDataAll.setName(textView.getContext().getString(R.string.all));
            cuisineList.add(filterDataAll);
            if (searchDataManager.getCuisine() != null) {
                for (CuisineRealmObject categoryRealmObject : searchDataManager.getCuisine()
                        ) {
                    FilterData filterData = new FilterData();
                    filterData.setName(categoryRealmObject.getName());
                    filterData.setFriendlyUrl(categoryRealmObject.getFriendlyUrl());
                    cuisineList.add(filterData);
                }
            }
        } else {
            fetchCategories();
        }
        dataSetListener.showFilterDialog(cuisineList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_cuisine));
    }

    public void displayCookingMethodList(TextView textView) {

        if (cookingMethodList == null) {
            cookingMethodList = new ArrayList<>();
            FilterData filterDataAll = new FilterData();
            filterDataAll.setName(textView.getContext().getString(R.string.all));
            cookingMethodList.add(filterDataAll);

            if (searchDataManager.getCategories() != null) {
                for (CategoryRealmObject categoryRealmObject : searchDataManager.getCategories()
                        ) {
                    FilterData filterData = new FilterData();
                    filterData.setName(categoryRealmObject.getName());
                    filterData.setFriendlyUrl(categoryRealmObject.getFriendlyUrl());
                    cookingMethodList.add(filterData);
                }
            }
        } else {
            fetchCategories();
        }
        dataSetListener.showFilterDialog(cookingMethodList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_method));
    }

    public void addSearchSuggestion(String keyword) {
        searchDataManager.insertSuggestion(keyword);
    }

    public List<SearchSuggestionRealmObject> getSearchSuggestion() {
        return searchDataManager.fetchSuggestionsKeyword();
    }

    @Override
    protected void destroy() {

    }


    public interface DataSetListener {
        void showFilterDialog(List<FilterData> filterDataList, String selectedFilter, TextView textView, String title);

        void updateSearchSuggestions(List<SearchSuggestionRealmObject> searchSuggestionList);
    }
}
