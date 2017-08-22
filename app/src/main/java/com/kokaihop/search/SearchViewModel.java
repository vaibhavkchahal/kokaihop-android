package com.kokaihop.search;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethod;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.EditorsChoiceRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.feed.SearchRecipeHeader;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
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
    private final Context context;
    private SearchDataManager searchDataManager;
    private List<FilterData> categoriesList;
    private List<FilterData> cuisineList;
    private List<FilterData> cookingMethodList;
    private List<FilterData> sortByList;
    private EditorsChoiceDataManager editorsChoiceDataManager;
    private String courseFriendlyUrl = "", cuisineFriendlyUrl = "", methodFriendlyUrl = "", searchKeyword = "", sortBy = "",
            courseName = "", cuisineName = "", methodName = "";
    //by default show all recipe with images
    private boolean withImage = true;

    private int EDITOR_CHOICE_SECTIONS_COUNT = 3;

    private ArrayList<Object> editorChoiceList1 = new ArrayList<>();
    private ArrayList<Object> editorChoiceList2 = new ArrayList<>();
    private ArrayList<Object> editorChoiceList3 = new ArrayList<>();

    private List<RecipeRealmObject> searchRecipeList = new ArrayList<>();

    public List<RecipeRealmObject> getRecipeList() {
        return searchRecipeList;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ArrayList<Object> getEditorChoiceList1() {
        return editorChoiceList1;
    }

    public void setEditorChoiceList1(ArrayList<Object> editorChoiceList1) {
        this.editorChoiceList1 = editorChoiceList1;
    }

    public ArrayList<Object> getEditorChoiceList2() {
        return editorChoiceList2;
    }

    public void setEditorChoiceList2(ArrayList<Object> editorChoiceList2) {
        this.editorChoiceList2 = editorChoiceList2;
    }

    public ArrayList<Object> getEditorChoiceList3() {
        return editorChoiceList3;
    }

    public void setEditorChoiceList3(ArrayList<Object> editorChoiceList3) {
        this.editorChoiceList3 = editorChoiceList3;
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
        this.context = context;
        searchDataManager = new SearchDataManager(context);
        editorsChoiceDataManager = new EditorsChoiceDataManager();
        fetchCategories();
        fetchCookingMethods();
        fetchCuisine();
        dataSetListener.updateSearchSuggestions(getSearchSuggestion());
    }

    public void getEditorChoiceForAllSections() {
        for (int section = 1; section <= EDITOR_CHOICE_SECTIONS_COUNT; section++) {
            getEditorChoiceFromServer(section);
        }
    }

    public void getEditorChoiceFromServer(final int section) {
        setProgressVisible(true);
        final String type = Constants.EDITOR_CHOICE_SECTION + section;
        getEditorChoiceFromDB(section);
        new EditorsChoiceApiHelper().getEditorsChoice(type, Constants.EDITOR_CHOICE_COUNT, Constants.EDITOR_CHOICE_TYPE, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject editorChoiceJson = new JSONObject(responseBody.string());
                    editorChoiceJson = editorChoiceJson.getJSONObject("data");
                    editorsChoiceDataManager.updateEditorsChoice(editorChoiceJson);
                    getEditorChoiceFromDB(section);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }
        });
    }

    public void getEditorChoiceFromDB(int section) {
        EditorsChoiceRealmObject editorsChoiceData = editorsChoiceDataManager.getEditorChoice(Constants.EDITOR_CHOICE_SECTION + section);
        if (editorsChoiceData != null) {
            switch (section) {
                case Constants.SECTION_1:
                    editorChoiceList1.clear();
                    editorChoiceList1.addAll(editorsChoiceData.getPayload());
                    break;
                case Constants.SECTION_2:
                    editorChoiceList2.clear();
                    editorChoiceList2.addAll(editorsChoiceData.getPayload());
                    break;
                case Constants.SECTION_3:
                    editorChoiceList3.clear();
                    editorChoiceList3.addAll(editorsChoiceData.getPayload());
                    break;

            }
            String categoryName = "";
            if (editorsChoiceData.getCategoryName() != null) {
                categoryName = editorsChoiceData.getCategoryName().getSv();
            }
            dataSetListener.showEditorsChoice(section, categoryName);
        }
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
            } else {
                fetchCategories();
            }
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
            } else {
                fetchCategories();
            }
        }
        dataSetListener.showFilterDialog(cuisineList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_cuisine), FilterType.CUISINE);
    }

    public void displaySortByList(View view) {
        String previousSelected = sortBy;
        if (sortByList == null) {
            sortByList = new ArrayList<>();
            String[] sortByArray = view.getContext().getResources().getStringArray(R.array.sort_by);
            for (String sortBy : sortByArray
                    ) {
                FilterData filterDataAll = new FilterData();
                filterDataAll.setName(sortBy);
                sortByList.add(filterDataAll);
            }
            if (sortBy.isEmpty()) {
                previousSelected = sortByList.get(0).getName();
            }
        }
        if (view.getTag() != null) {
            previousSelected = view.getTag().toString();
        }
        dataSetListener.showFilterDialog(sortByList, previousSelected, view, view.getContext().getResources().getString(R.string.sort_by), FilterType.SORT_BY);
    }


    public void showWithImages(View view, View parentView) {
        boolean isSelected;
        String msg;
        String label;
        if (view.getBackground().getConstantState()
                == ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.ic_picture, null).getConstantState()) {
            isSelected = false;
            msg = view.getContext().getResources().getString(R.string.show_all_recipes);
            label = context.getString(R.string.search_image_off_label);

        } else {
            isSelected = true;
            msg = view.getContext().getResources().getString(R.string.show_recipe_with_images);
            label = context.getString(R.string.search_image_on_label);

        }
        Activity activity = (Activity) context;
        GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.search_category), context.getString(R.string.search_image_action), label);
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
            } else {
                fetchCategories();
            }
        }
        dataSetListener.showFilterDialog(cookingMethodList, textView.getText().toString(), textView, textView.getContext().getResources().getString(R.string.select_method), FilterType.METHOD);
    }

    public void fetchNewlyAddedRecipeWithAds() {
        setProgressVisible(true);
        searchDataManager.fetchNewlyAddedRecipe(withImage, new SearchDataManager.OnCompleteListener() {
            @Override
            public void onSearchComplete(List<RecipeRealmObject> recipeList) {
                setProgressVisible(false);
                searchRecipeList = recipeList;
                List<Object> recipeListwithAds = insertAdsInList(recipeList);
                dataSetListener.showRecipesList(recipeListwithAds);
            }
        });
    }

    public List<Object> insertAdsInList(List<RecipeRealmObject> recipeList) {
        List<Object> recipeListwithAds = new ArrayList<>();
        SearchRecipeHeader searchRecipeHeader = new SearchRecipeHeader();
        searchRecipeHeader.setCount(String.valueOf(recipeList.size()));
        recipeListwithAds.add(searchRecipeHeader);
        recipeListwithAds.addAll(recipeList);
        AppUtility utility = new AppUtility();
        utility.addAdvtInRecipeList(recipeListwithAds, AppCredentials.SEARCH_ADS_UNIT_IDS, context);
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
        if (filterMap.isEmpty() && sortBy.isEmpty() && searchKeyword.isEmpty()) {
            dataSetListener.showSuggestionView();

        } else {
            setProgressVisible(true);
            if (sortBy.isEmpty()) {
                sortBy = context.getResources().getString(R.string.best_rating);
            }
            trackGAEvent(sortBy);
            searchDataManager.selectedFiltersSearchQuery(filterMap, withImage, sortBy, searchKeyword,
                    new SearchDataManager.OnCompleteListener() {
                        @Override
                        public void onSearchComplete(List<RecipeRealmObject> recipeList) {
                            setProgressVisible(false);
                            searchRecipeList = recipeList;
                            List<Object> recipeListwithAds = insertAdsInList(recipeList);
                            dataSetListener.showRecipesList(recipeListwithAds);
                        }
                    });
        }

    }

    private void trackGAEvent(String sortBy) {
        String label = "";
        if (sortBy.equals(context.getResources().getString(R.string.best_rating))) {
            label = context.getResources().getString(R.string.rated_label);

        } else if (sortBy.equals(context.getResources().getString(R.string.comments))) {
            label = context.getResources().getString(R.string.comments_label);


        } else if (sortBy.equals(context.getResources().getString(R.string.popular))) {
            label = context.getResources().getString(R.string.relevance_label);


        } else if (sortBy.equals(context.getResources().getString(R.string.recommended))) {
            label = context.getResources().getString(R.string.recomended_label);


        } else if (sortBy.equals(context.getResources().getString(R.string.latest))) {
            label = context.getResources().getString(R.string.most_recent_label);


        } else if (sortBy.equals(context.getResources().getString(R.string.title_a_z))) {
            label = context.getResources().getString(R.string.alphabatical_label);

        } else if (sortBy.equals(context.getResources().getString(R.string.title_z_a))) {
            label = context.getResources().getString(R.string.reverse_alphabatical_label);
        }
        if (!label.isEmpty()) {
            Activity activity = (Activity) context;
            GoogleAnalyticsHelper.trackEventAction(context.getResources().getString(R.string.search_category), context.getResources().getString(R.string.search_sorted_action), label);

        }


    }

    public interface DataSetListener {
        void showFilterDialog(List<FilterData> filterDataList, String selectedFilter, View textView, String title, FilterType filterType);

        void showWithImageDialog(View childView, View view, boolean selected, String msg);

        void updateSearchSuggestions(List<SearchSuggestionRealmObject> searchSuggestionList);

        void showRecipesList(List<Object> recipeList);

        void showSuggestionView();

        void showEditorsChoice(int section, String categoryName);
    }

}
