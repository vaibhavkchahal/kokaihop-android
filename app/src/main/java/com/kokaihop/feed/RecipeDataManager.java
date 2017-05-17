package com.kokaihop.feed;

import android.util.Log;

import com.kokaihop.database.Recipe;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.utility.ApiConstants;

import java.util.ArrayList;
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


    public void insertOrUpdateData(RecipeResponse recipeResponse) {
        realm.beginTransaction();
        List<Recipe> recipeList = new ArrayList<>();
        for (RecipeInfo recipeInfo : recipeResponse.getRecipeDetailsList()) {
            Recipe recipe = realm.where(Recipe.class)
                    .equalTo("_id", recipeInfo.getRecipe().get_id()).findFirst();
            recipe.setBadgeType(recipeInfo.getRecipe().getBadgeType());
            if (recipeResponse.getMyLikes() != null) {
                boolean isLiked = recipeResponse.getMyLikes().contains(recipe.get_id());
                recipe.setFavorite(isLiked);
            }
            recipeList.add(recipe);
//            realm.insertOrUpdate(recipe);
            Log.d("id", recipeInfo.getRecipe().get_id());
        }
        realm.copyToRealmOrUpdate(recipeList);
//        recipeDataListener.onTransactionComplete(true);
        realm.commitTransaction();
    }

    public interface RecipeDataListener {
        void onTransactionComplete(boolean executed);
    }


}
