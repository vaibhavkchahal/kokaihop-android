package com.kokaihop.search;

import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethod;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchDataManager {
    private final int MAX_SUGGESTIONS = 4;


    private Realm realm;

    public SearchDataManager() {
        realm = Realm.getDefaultInstance();

    }

    public RealmResults<CategoryRealmObject> getCategories() {
        RealmResults<CategoryRealmObject> categoriesList = realm.where(CategoryRealmObject.class).findAll();
        return categoriesList;
    }

    public RealmResults<CuisineRealmObject> getCuisine() {
        RealmResults<CuisineRealmObject> CuisineList = realm.where(CuisineRealmObject.class).findAll();
        return CuisineList;
    }

    public RealmResults<CookingMethod> getCookingMethods() {
        RealmResults<CookingMethod> cookingMethodList = realm.where(CookingMethod.class).findAll();
        return cookingMethodList;
    }

    public void insertOrUpdateRecipeCategories(final JSONObject jsonObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    JSONArray recipeCategoriesJSONArray = jsonObject.getJSONArray("recipeCategories");
                    realm.createOrUpdateAllFromJson(CategoryRealmObject.class, recipeCategoriesJSONArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void insertOrUpdateRecipeCuisine(final JSONObject jsonObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    JSONArray recipeCategoriesJSONArray = jsonObject.getJSONArray("cuisines");
                    realm.createOrUpdateAllFromJson(CuisineRealmObject.class, recipeCategoriesJSONArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void insertOrUpdateRecipeCookingMethods(final JSONObject jsonObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    JSONArray recipeCategoriesJSONArray = jsonObject.getJSONArray("cookingMethods");
                    realm.createOrUpdateAllFromJson(CookingMethod.class, recipeCategoriesJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void insertSuggestion(String keyword) {
        realm.beginTransaction();
        SearchSuggestionRealmObject searchSuggestionRealmObject = new SearchSuggestionRealmObject();
        searchSuggestionRealmObject.setKeyword(keyword);
        searchSuggestionRealmObject.setTimeStamp(new Date().getTime());
        realm.insertOrUpdate(searchSuggestionRealmObject);
        realm.commitTransaction();
        RealmResults<SearchSuggestionRealmObject> searchSuggestions = realm.where(SearchSuggestionRealmObject.class).findAll().sort("timeStamp");
        if (searchSuggestions.size() > MAX_SUGGESTIONS) {
            realm.beginTransaction();
            searchSuggestions.deleteFirstFromRealm();
            realm.commitTransaction();
        }

    }

    public ArrayList<SearchSuggestionRealmObject> fetchSuggestionsKeyword() {
        RealmResults<SearchSuggestionRealmObject> realmResult = realm.where(SearchSuggestionRealmObject.class).findAll().sort("timeStamp", Sort.DESCENDING);
        ArrayList<SearchSuggestionRealmObject> historyRealmObjects = (ArrayList<SearchSuggestionRealmObject>) realm.copyFromRealm(realmResult);
        return historyRealmObjects;
    }
}
