package com.kokaihop.search;

import android.content.Context;
import android.os.AsyncTask;

import com.kokaihop.database.RecipeRealmObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class SearchKeywordAsync extends AsyncTask<Void, Void, List<RecipeRealmObject>> {
    private HashMap<String, String> filterMap;
    private Context context;
    private OnCompleteListener onCompleteListener;
    private SearchDataManager searchDataManager;
    private QUERY_TYPE query_type;
    private boolean withImage;
    private String sortBy, searchKeyword;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public enum QUERY_TYPE {
        SEARCH,
        NEWLY_ADDED_RECIPE
    }


    //    filterMap, withImage, sortBy, searchKeyword,
    public SearchKeywordAsync(SearchDataManager searchDataManager, QUERY_TYPE query_type,
                              HashMap<String, String> filterMap, boolean withImage, String sortBy,
                              String searchKeyword) {
        this.searchDataManager = searchDataManager;
        this.query_type = query_type;
        this.filterMap = filterMap;
        this.withImage = withImage;
        this.sortBy = sortBy;
        this.searchKeyword = searchKeyword;

    }


    public SearchKeywordAsync(SearchDataManager searchDataManager, QUERY_TYPE query_type,
                              boolean withImage) {
        this.searchDataManager = searchDataManager;
        this.query_type = query_type;
        this.withImage = withImage;

    }

    @Override
    protected List<RecipeRealmObject> doInBackground(Void... params) {
        List<RecipeRealmObject> recipeList = null;
        switch (query_type) {
            case SEARCH:
//                recipeList = searchDataManager.selectedFiltersSearchQuery(filterMap, withImage, sortBy, searchKeyword);
                break;
            case NEWLY_ADDED_RECIPE:
//                recipeList = searchDataManager.fetchNewlyAddedRecipe(withImage);
                break;
        }
        return recipeList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(List<RecipeRealmObject> uploadResult) {
        onCompleteListener.onSearchComplete(uploadResult);

    }


    /**
     * Inteface for the caller
     */
    public interface OnCompleteListener {
        void onSearchComplete(List<RecipeRealmObject> recipeList);
    }
}
