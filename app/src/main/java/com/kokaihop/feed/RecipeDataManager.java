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

    public RecipeDataManager() {
        realm = Realm.getDefaultInstance();

    }

    public RealmResults<Recipe> fetchRecipe(ApiConstants.BadgeType badgeType) {

        RealmResults<Recipe> recipeList = realm.where(Recipe.class).equalTo("badgeType", badgeType.value).findAll();
        return recipeList;
    }


    public void insertOrUpdateData(final List<Recipe> realmResults) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmResults);
//        Collection<Recipe> recipes=realm.copyToRealmOrUpdate(realmResults);
        realm.commitTransaction();
    }


}
