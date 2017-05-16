package com.kokaihop.feed;

import com.kokaihop.database.Recipe;
import com.kokaihop.utility.ApiConstants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeDataManager {
    private Realm realm;
    private RecipeDataListener recipeDataListener;

    public RecipeDataManager(RecipeDataListener recipeDataListener) {
        this.recipeDataListener = recipeDataListener;
        realm = Realm.getDefaultInstance();

    }

    public RealmResults<Recipe> fetchRecipe(ApiConstants.BadgeType badgeType) {

        RealmResults<Recipe> recipeList = realm.where(Recipe.class).equalTo("badgeType", badgeType.value).findAll();
        return recipeList;
    }


    public void insertOrUpdateData(final List<Recipe> realmResults) {
        realm.beginTransaction();
        for (Recipe recipe : realmResults) {
            Recipe recipeInDatabase = realm.where(Recipe.class)
                    .equalTo("_id", recipe.get_id()).findFirst();
            recipeInDatabase.setBadgeType(recipe.getBadgeType());
            realm.insertOrUpdate(recipeInDatabase);
        }
        recipeDataListener.onTransactionComplete(true);
        realm.commitTransaction();
    }

    public interface RecipeDataListener {
        void onTransactionComplete(boolean executed);
    }


}
