package com.kokaihop.search;

import android.content.Context;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethod;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.utility.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchDataManager {
    private final int MAX_SUGGESTIONS = 4;
    private Context context;
    private RealmResults<RecipeRealmObject> recipeRealmResult;

    private Realm realm;

    public SearchDataManager(Context context) {
        realm = Realm.getDefaultInstance();
        this.context = context;

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

    public void fetchNewlyAddedRecipe(boolean withImage, final OnCompleteListener onCompleteListener) {
        RealmQuery<RecipeRealmObject> query = realm.where(RecipeRealmObject.class);
        if (withImage) {
            query.isNotNull("mainImage");
        }
        Logger.e("time before query", new Date().toString());

        recipeRealmResult = query.findAllSortedAsync("dateCreated", Sort.DESCENDING);
        RealmChangeListener realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                if (recipeRealmResult != null) {
                    Logger.e("time after query", new Date().toString());
                    onCompleteListener.onSearchComplete(recipeRealmResult);
                    recipeRealmResult.removeAllChangeListeners();

                }
            }
        };
        recipeRealmResult.addChangeListener(realmChangeListener);
    }

    public void selectedFiltersSearchQuery(Map<String, String> filterMap, boolean withImage,
                                           final String sortBy, String keyword, final OnCompleteListener onCompleteListener) {
        RealmQuery<RecipeRealmObject> query;
        query = realm.where(RecipeRealmObject.class);
        if (keyword != null && !keyword.isEmpty()) {
            query = query.contains("title", keyword, Case.INSENSITIVE).or().contains("ingredients.name", keyword, Case.INSENSITIVE);
        }
        if (withImage) {
            query = query.isNotNull("mainImage");
        }
        if (filterMap.size() > 0) {
            query.beginGroup();
            List<String> keyList = new ArrayList<>(filterMap.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                String value = filterMap.get(key);
                query = query.equalTo(key, value);
               /* if (i != keyList.size() - 1) {
                    query = query.and();
                }*/
            }
            query.endGroup();
        }
        recipeRealmResult = sortFilter(query, sortBy);


        final RealmChangeListener realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                if (recipeRealmResult != null) {
                    Logger.e("time after query", new Date().toString());
                    if (!sortBy.isEmpty() && sortBy.equals(context.getResources().getString(R.string.best_rating))) {
                        List<RecipeRealmObject> ratingMoreThan5List = recipeRealmResult.where().greaterThanOrEqualTo("rating.raters", 5).findAllSorted("rating.average", Sort.DESCENDING);
                        List<RecipeRealmObject> ratingLessThan5List = recipeRealmResult.where().lessThan("rating.raters", 5).findAllSorted("rating.average", Sort.DESCENDING);
                        List<RecipeRealmObject> ratingNullList = recipeRealmResult.where().isNull("rating").findAll();

                        List<RecipeRealmObject> recipeList = new ArrayList<>();
                        recipeList.addAll(ratingMoreThan5List);
                        recipeList.addAll(ratingLessThan5List);
                        recipeList.addAll(ratingNullList);
                        onCompleteListener.onSearchComplete(recipeList);
                    } else {
                        onCompleteListener.onSearchComplete(recipeRealmResult);
                    }
                    recipeRealmResult.removeAllChangeListeners();

                }
            }
        };
        recipeRealmResult.addChangeListener(realmChangeListener);

    }


    private RealmResults<RecipeRealmObject> sortFilter(RealmQuery<RecipeRealmObject> query, String sortBy) {
        RealmResults<RecipeRealmObject> recipeRealmResult = null;

        if (!sortBy.isEmpty()) {
            String sortField = "title";
            Sort sort = Sort.ASCENDING;
            if (sortBy.equals(context.getResources().getString(R.string.best_rating))) {
                sortField = "rating.average";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.comments))) {

                sortField = "counter.comments";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.popular))) {
                sortField = "counter.viewed";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.recommended))) {
                sortField = "counter.likes";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.latest))) {
                sortField = "dateCreated";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.title_a_z))) {
                sortField = "title";
                sort = Sort.ASCENDING;
            } else if (sortBy.equals(context.getResources().getString(R.string.title_z_a))) {
                sortField = "title";
                sort = Sort.DESCENDING;

            }
            Logger.e("time before query", new Date().toString());
            recipeRealmResult = query.findAllSortedAsync(sortField, sort);


        } else {
            Logger.e("time before query", new Date().toString());
            recipeRealmResult = query.findAllAsync();
        }
        return recipeRealmResult;

    }


    public ArrayList<SearchSuggestionRealmObject> fetchSuggestionsKeyword() {
        RealmResults<SearchSuggestionRealmObject> realmResult = realm.where(SearchSuggestionRealmObject.class).findAll().sort("timeStamp", Sort.DESCENDING);
        ArrayList<SearchSuggestionRealmObject> historyRealmObjects = (ArrayList<SearchSuggestionRealmObject>) realm.copyFromRealm(realmResult);
        return historyRealmObjects;
    }


    public interface OnCompleteListener {
        void onSearchComplete(List<RecipeRealmObject> recipeList);
    }
}
