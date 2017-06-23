package com.kokaihop.search;

import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethod;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.feed.Recipe;

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

    public ArrayList<Recipe> fetchNewlyAddedRecipe() {
        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class)
                .findAllSorted("dateCreated", Sort.DESCENDING);
        ArrayList<Recipe> recipeList = new ArrayList<>(recipeRealmObjectList.size());
        for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList) {
            recipeList.add(getRecipe(recipeRealmObject));
        }
        return recipeList;
    }

    public Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        Recipe recipe = new Recipe();
        recipe.set_id(recipeRealmObject.get_id());
        recipe.setTitle(recipeRealmObject.getTitle());
        recipe.setType(recipeRealmObject.getType());
        recipe.setCreatedById(recipeRealmObject.getCreatedBy().getId());
        recipe.setCreatedByName(recipeRealmObject.getCreatedBy().getName());
        recipe.setCreatedByProfileImageId(recipeRealmObject.getCreatedBy().getProfileImageId());
        recipe.setCoverImage(recipeRealmObject.getCoverImage());
        if (recipeRealmObject.getMainImage() != null) {
            recipe.setMainImagePublicId(recipeRealmObject.getMainImage().getPublicId());
        }
        recipe.setFavorite(recipeRealmObject.isFavorite());
        recipe.setLikes(String.valueOf(recipeRealmObject.getCounter().getLikes()));
        if (recipeRealmObject.getRating() != null) {
            recipe.setRatingAverage(recipeRealmObject.getRating().getAverage());

        }
        recipe.setBadgeDateCreated(recipeRealmObject.getBadgeDateCreated());
        recipe.setComments(recipeRealmObject.getCounter().getComments());
        recipe.setBadgeType(recipeRealmObject.getBadgeType());
        recipe.setLastUpdated(recipeRealmObject.getLastUpdated());
        return recipe;

    }

    public ArrayList<SearchSuggestionRealmObject> fetchSuggestionsKeyword() {
        RealmResults<SearchSuggestionRealmObject> realmResult = realm.where(SearchSuggestionRealmObject.class).findAll().sort("timeStamp", Sort.DESCENDING);
        ArrayList<SearchSuggestionRealmObject> historyRealmObjects = (ArrayList<SearchSuggestionRealmObject>) realm.copyFromRealm(realmResult);
        return historyRealmObjects;
    }
}
