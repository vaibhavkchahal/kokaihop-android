package com.kokaihop.cookbooks;

import com.google.gson.Gson;
import com.kokaihop.database.CookbookRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.utility.JSONObjectUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 27/6/17.
 */

public class CookbooksDataManager {
    Realm realm;
    Gson gson;

    public CookbooksDataManager() {
        this.realm = Realm.getDefaultInstance();
        gson = new Gson();
    }

    public void insertOrUpdateRecipeIntoCookbooks(final JSONArray recipes, String userFriendlyUrl, String cookbookFriendlyUrl) {

        final UserRealmObject userRealmObject = realm.where(UserRealmObject.class)
                .equalTo("friendlyUrl", userFriendlyUrl)
                .findFirst();

        if (userRealmObject != null) {
            String cookbookId = getCookbookIdOfUser(userRealmObject.getRecipeCollections(), cookbookFriendlyUrl);
            if (cookbookId != null && !cookbookFriendlyUrl.isEmpty()) {
                CookbookRealmObject cookbookRealmObject = realm.where(CookbookRealmObject.class).equalTo("_id", cookbookId).findFirst();
                if (cookbookRealmObject != null) {
                    realm.beginTransaction();
                    for (int i = 0; i < recipes.length(); i++) {
                        try {
                            JSONObjectUtility jsonObjectUtility = new JSONObjectUtility();
                            JSONObject jsonObject = recipes.getJSONObject(i);
                            jsonObject = jsonObjectUtility.updateCookingStepsInRecipe(jsonObject);
                            jsonObject = jsonObjectUtility.removeKeyFromJSON(jsonObject, "similarRecipes");
                            realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, jsonObject);

                            RecipeRealmObject recipeRealmObject = gson.fromJson(jsonObject.toString(), RecipeRealmObject.class);
                            if (!recipeAlreadyExists(cookbookRealmObject.getRecipes(), recipeRealmObject)) {
                                cookbookRealmObject.getRecipes().add(recipeRealmObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    realm.commitTransaction();
                }
            }
        }
    }

    public ArrayList<Recipe> getRecipesOfCookbook(String userFriendlyUrl, String cookbookFriendlyUrl) {
        ArrayList<Recipe> recipeList = new ArrayList<>();

        final UserRealmObject userRealmObject = realm.where(UserRealmObject.class)
                .equalTo("friendlyUrl", userFriendlyUrl)
                .findFirst();

        if (userRealmObject != null) {
            String cookbookId = getCookbookIdOfUser(userRealmObject.getRecipeCollections(), cookbookFriendlyUrl);
            if (cookbookId != null && !cookbookFriendlyUrl.isEmpty()) {
                CookbookRealmObject cookbookRealmObject = realm.where(CookbookRealmObject.class).equalTo("_id", cookbookId).findFirst();
                if (cookbookRealmObject != null) {
                    for (RecipeRealmObject realmObject : cookbookRealmObject.getRecipes()) {
                        Recipe recipe = new Recipe();
                        recipe.set_id(realmObject.get_id());
                        recipe.setTitle(realmObject.getTitle());
                        if (realmObject.getCreatedBy() != null) {
                            recipe.setCreatedByName(realmObject.getCreatedBy().getName());
                        }
                        if (realmObject.getRating() != null) {
                            recipe.setRatingAverage(realmObject.getRating().getAverage());
                        }
                        if (realmObject.getMainImage() != null) {
                            recipe.setMainImagePublicId(realmObject.getMainImage().getPublicId());
                        }
                        recipeList.add(recipe);
                    }
                }
            }
        }
        return recipeList;
    }

    private String getCookbookIdOfUser(RealmList<CookbookRealmObject> recipeCollections, String cookbookFriendlyUrl) {
        for (CookbookRealmObject cookbook : recipeCollections) {
            if (cookbook.getFriendlyUrl().equals(cookbookFriendlyUrl)) {
                return cookbook.get_id();
            }
        }
        return null;
    }

    public boolean recipeAlreadyExists(RealmList<RecipeRealmObject> list, RecipeRealmObject recipeRealmObject) {
        for (RecipeRealmObject userInList : list) {
            if (recipeRealmObject.get_id().equals(userInList.get_id()))
                return true;
        }
        return false;
    }

    public String getIdOfCookbook(String cookbookFriendlyUrl) {
        CookbookRealmObject cookbook = realm.where(CookbookRealmObject.class)
                .equalTo("friendlyUrl", cookbookFriendlyUrl)
                .findFirst();
        if(cookbook==null){
            return null;
        }
        return cookbook.get_id();
    }


    public void deleteCookbook(String cookbookFriendlyUrl){
        realm.beginTransaction();
        realm.where(CookbookRealmObject.class).equalTo("friendlyUrl",cookbookFriendlyUrl)
                .findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
