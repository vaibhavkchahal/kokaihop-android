package com.kokaihop.search;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethod;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.SearchRecipeHeader;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private List<FilterData> sortByList;
    private String courseFriendlyUrl = "", cuisineFriendlyUrl = "", methodFriendlyUrl = "", searchKeyword = "", sortBy = "";
    //by default show all recipe with images
    private boolean withImage = true;

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }


    public static enum FilterType {
        COURSE,
        CUISINE,
        METHOD,
        SORT_BY

    }

    public void setCourseFriendlyUrl(String courseFriendlyUrl) {
        this.courseFriendlyUrl = courseFriendlyUrl;
    }

    public void setCuisineFriendlyUrl(String cuisineFriendlyUrl) {
        this.cuisineFriendlyUrl = cuisineFriendlyUrl;
    }

    public void setMethodFriendlyUrl(String methodFriendlyUrl) {
        this.methodFriendlyUrl = methodFriendlyUrl;
    }

    public void setWithImage(boolean withImage) {
        this.withImage = withImage;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SearchViewModel(DataSetListener dataSetListener, Context context) {
        this.dataSetListener = dataSetListener;
        searchDataManager = new SearchDataManager(context);
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
        dataSetListener.showFilterDialog(categoriesList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_course), FilterType.COURSE);
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
        dataSetListener.showFilterDialog(cuisineList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_cuisine), FilterType.CUISINE);
    }

    public void displaySortByList(View view) {
        String previousSelected = "";
        if (sortByList == null) {
            sortByList = new ArrayList<>();
            String[] sortByArray = view.getContext().getResources().getStringArray(R.array.sort_by);

            for (String sortBy : sortByArray
                    ) {
                FilterData filterDataAll = new FilterData();
                filterDataAll.setName(sortBy);
                sortByList.add(filterDataAll);
            }

            previousSelected = sortByList.get(0).getName();
        }
        if (view.getTag() != null) {
            previousSelected = view.getTag().toString();
        }
        dataSetListener.showFilterDialog(sortByList, previousSelected, view, view.getContext().getResources().getString(R.string.sort_by), FilterType.SORT_BY);
    }


    public void showWithImages(View view, View parentView) {
        boolean isSelected;
        String msg;

        if (view.getBackground().getConstantState()
                == ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.ic_picture, null).getConstantState()) {
            isSelected = false;
            msg = view.getContext().getResources().getString(R.string.show_all_recipes);
        } else {
            isSelected = true;
            msg = view.getContext().getResources().getString(R.string.show_recipe_with_images);
        }
        dataSetListener.showWithImageDialog(view, parentView, isSelected, msg);
    }


    public void displayCookingMethodList(TextView textView) {

        if (cookingMethodList == null) {
            cookingMethodList = new ArrayList<>();
            FilterData filterDataAll = new FilterData();
            filterDataAll.setName(textView.getContext().getString(R.string.all));
            cookingMethodList.add(filterDataAll);

            if (searchDataManager.getCookingMethods() != null) {
                for (CookingMethod cookingMethod : searchDataManager.getCookingMethods()
                        ) {
                    FilterData filterData = new FilterData();
                    filterData.setName(cookingMethod.getName());
                    filterData.setFriendlyUrl(cookingMethod.getFriendlyUrl());
                    cookingMethodList.add(filterData);
                }
            }
        } else {
            fetchCategories();
        }
        dataSetListener.showFilterDialog(cookingMethodList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_method), FilterType.METHOD);
    }

    public void fetchNewlyAddedRecipeWithAds() {
        setProgressVisible(true);
        SearchKeywordAsync searchKeywordAsync = new SearchKeywordAsync(searchDataManager, SearchKeywordAsync.QUERY_TYPE.NEWLY_ADDED_RECIPE,
                withImage);
        searchKeywordAsync.setOnCompleteListener(onSearchCompleteListener);
        searchKeywordAsync.execute();


    }

    private List<Object> insertAdsInList(List<Recipe> recipeList) {
        List<Object> recipeListwithAds = new ArrayList<>();
        int count = recipeList.size();
        SearchRecipeHeader searchRecipeHeader = new SearchRecipeHeader();
        searchRecipeHeader.setCount(String.valueOf(count));
        recipeListwithAds.add(searchRecipeHeader);
        recipeListwithAds.addAll(recipeList);
        int prevPos = 0;
        for (int position = 0; position < recipeListwithAds.size(); position++) {
            if (position == 3 || (prevPos + 7) == position) {
                prevPos = position;
                AdvtDetail advtDetail = new AdvtDetail();
                recipeListwithAds.add(position, advtDetail);
            }
        }
        return recipeListwithAds;
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

    public void setCurrentSelectedFilter(FilterData filterData, SearchViewModel.FilterType filterType) {
        switch (filterType) {
            case COURSE:
                setCourseFriendlyUrl(filterData.getFriendlyUrl());
                break;
            case CUISINE:
                setCuisineFriendlyUrl(filterData.getFriendlyUrl());
                break;
            case METHOD:
                setMethodFriendlyUrl(filterData.getFriendlyUrl());
                break;
            case SORT_BY:
                setSortBy(filterData.getName());
                break;
        }


    }

    public void search() {
        setProgressVisible(true);
        HashMap<String, String> filterMap = new HashMap<>();
        if (courseFriendlyUrl != null && !courseFriendlyUrl.isEmpty()) {
            filterMap.put("category.friendlyUrl", courseFriendlyUrl);
        }
        if (cuisineFriendlyUrl != null && !cuisineFriendlyUrl.isEmpty()) {
            filterMap.put("cuisine.friendlyUrl", cuisineFriendlyUrl);

        }
        if (methodFriendlyUrl != null && !methodFriendlyUrl.isEmpty()) {
            filterMap.put("cookingMethod.friendlyUrl", methodFriendlyUrl);

        }
        SearchKeywordAsync searchKeywordAsync = new SearchKeywordAsync(searchDataManager, SearchKeywordAsync.QUERY_TYPE.SEARCH,
                filterMap, withImage, sortBy, searchKeyword);
        searchKeywordAsync.setOnCompleteListener(onSearchCompleteListener);
        searchKeywordAsync.execute();
    }


    SearchKeywordAsync.OnCompleteListener onSearchCompleteListener = new SearchKeywordAsync.OnCompleteListener() {
        @Override
        public void onSearchComplete(List<Recipe> recipeList) {
            setProgressVisible(false);
            List<Object> recipeListwithAds = insertAdsInList(recipeList);
            dataSetListener.showRecipesList(recipeListwithAds);
        }
    };

    public interface DataSetListener {
        void showFilterDialog(List<FilterData> filterDataList, String selectedFilter, View textView, String title, FilterType filterType);

        void showWithImageDialog(View childView, View view, boolean selected, String msg);

        void updateSearchSuggestions(List<SearchSuggestionRealmObject> searchSuggestionList);

        void showRecipesList(List<Object> recipeList);
    }
}
