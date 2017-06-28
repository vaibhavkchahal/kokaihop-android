package com.kokaihop.search;

import android.content.Context;

import com.altaworks.kokaihop.ui.R;
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
import java.util.HashMap;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class SearchDataManager {
    private final int MAX_SUGGESTIONS = 4;
    private Context context;


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

    public ArrayList<Recipe> fetchNewlyAddedRecipe(boolean withImage) {

        RealmQuery<RecipeRealmObject> query = realm.where(RecipeRealmObject.class);

        if (withImage) {
            query.isNotNull("mainImage");
//            query = query.notEqualTo("mainImage.publicId", "");
        }
        RealmResults<RecipeRealmObject> recipeRealmObjectList = query.findAllSortedAsync("dateCreated", Sort.DESCENDING);

//        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class).isNotNull("mainImage").findAllSortedAsync("dateCreated", Sort.DESCENDING);
        ArrayList<Recipe> recipeList = new ArrayList<>(recipeRealmObjectList.size());
        for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList) {
            recipeList.add(getRecipe(recipeRealmObject));
        }

        return recipeList;
    }


    public List<Recipe> selectedFiltersSearchQuery(String course, String cuisine, String method, boolean withImage, String sortBy, String keyword) {
        HashMap<String, String> filterMap = new HashMap<>();
        if (course != null && !course.isEmpty()) {
            filterMap.put("category.friendlyUrl", course);
        }
        if (cuisine != null && !cuisine.isEmpty()) {
            filterMap.put("cuisine.friendlyUrl", cuisine);

        }
        if (method != null && !method.isEmpty()) {
            filterMap.put("cookingMethod.friendlyUrl", method);

        }

        RealmQuery<RecipeRealmObject> query;
        query = realm.where(RecipeRealmObject.class);

        if (keyword != null && !keyword.isEmpty()) {
            query = query.contains("title", keyword, Case.INSENSITIVE).or().contains("ingredients.name", keyword, Case.INSENSITIVE);
//            recipeRealmObjectList = realm.where(RecipeRealmObject.class).like("title", keyword, Case.INSENSITIVE).or().like("ingredients.name", keyword, Case.INSENSITIVE).findAll();

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
        RealmResults<RecipeRealmObject> recipeRealmResult = null;
        if (!sortBy.isEmpty()) {
            String sortField = "title";
            Sort sort = Sort.ASCENDING;

            if (sortBy.equals(context.getResources().getString(R.string.title_a_z))) {
                sortField = "title";
                sort = Sort.ASCENDING;
            } else if (sortBy.equals(context.getResources().getString(R.string.title_z_a))) {
                sortField = "title";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.most_recent))) {
                sortField = "dateCreated";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.most_commented))) {

                sortField = "comments";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.most_rated))) {

                sortField = "rating.raters";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.popular))) {

                sortField = "rating.average";
                sort = Sort.DESCENDING;

            } else if (sortBy.equals(context.getResources().getString(R.string.relevance))) {

            }
            recipeRealmResult = query.findAllSortedAsync(sortField, sort);


        } else {
            recipeRealmResult = query.findAllAsync();

        }
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeRealmObject recipeRealmObject : recipeRealmResult) {
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
