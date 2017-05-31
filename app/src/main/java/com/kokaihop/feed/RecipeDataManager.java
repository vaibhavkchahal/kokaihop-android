package com.kokaihop.feed;

import android.util.Log;

import com.kokaihop.database.CounterRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.utility.ApiConstants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeDataManager {
    private Realm realm;

    public RecipeDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public List<Recipe> fetchRecipe(ApiConstants.BadgeType badgeType) {
        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class)
                .equalTo("badgeType", badgeType.value)
                .findAllSorted("badgeDateCreated", Sort.DESCENDING);
        List<Recipe> recipeList = new ArrayList<>(recipeRealmObjectList.size());
        for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList) {
            recipeList.add(getRecipe(recipeRealmObject));
        }
        return recipeList;
    }

    private Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        Recipe recipe = new Recipe();
        recipe.set_id(recipeRealmObject.get_id());
        recipe.setTitle(recipeRealmObject.getTitle());
        recipe.setType(recipeRealmObject.getType());
        recipe.setCreatedById(recipeRealmObject.getCreatedByRealmObject().getId());
        recipe.setCreatedByName(recipeRealmObject.getCreatedByRealmObject().getName());
        recipe.setCreatedByProfileImageId(recipeRealmObject.getCreatedByRealmObject().getProfileImageId());
        if(recipeRealmObject.getMainImageRealmObject()!=null)
        {
            recipe.setMainImagePublicId(recipeRealmObject.getMainImageRealmObject().getPublicId());
        }
        recipe.setFavorite(recipeRealmObject.isFavorite());
        recipe.setLikes(recipeRealmObject.getCounterRealmObject().getLikes());
        if(recipeRealmObject.getRatingRealmObject()!=null)
        {
            recipe.setRatingAverage(recipeRealmObject.getRatingRealmObject().getAverage());

        }
        recipe.setBadgeDateCreated(recipeRealmObject.getBadgeDateCreated());
        recipe.setComments(recipeRealmObject.getCounterRealmObject().getComments());
        recipe.setBadgeType(recipeRealmObject.getBadgeType());


        return recipe;

    }


    public void insertOrUpdateData(RecipeResponse recipeResponse) {
        realm.beginTransaction();
        List<RecipeRealmObject> recipeRealmObjectList = new ArrayList<>();
        for (RecipeInfo recipeInfo : recipeResponse.getRecipeDetailsList()) {
            RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                    .equalTo("_id", recipeInfo.getRecipeRealmObject().get_id()).findFirst();
            if (recipeRealmObject != null) {
                recipeRealmObject.setBadgeType(recipeInfo.getRecipeRealmObject().getBadgeType());
                recipeRealmObject.setCounterRealmObject(updateCounter(recipeInfo.getRecipeRealmObject()));
                recipeRealmObject.setBadgeDateCreated(recipeInfo.getRecipeRealmObject().getDateCreated());
                if (recipeResponse.getMyLikes() != null) {
                    boolean isLiked = recipeResponse.getMyLikes().contains(recipeRealmObject.get_id());
                    recipeRealmObject.setFavorite(isLiked);
                }
            } else {
                recipeRealmObject = recipeInfo.getRecipeRealmObject();
            }
            recipeRealmObject.setLastUpdated(System.currentTimeMillis());
            recipeRealmObjectList.add(recipeRealmObject);
            Log.d("id", recipeInfo.getRecipeRealmObject().get_id());
        }
        realm.insertOrUpdate(recipeRealmObjectList);
//        recipeDataListener.onTransactionComplete(true);
        realm.commitTransaction();
    }

    private CounterRealmObject updateCounter(RecipeRealmObject recipeRealmObject) {
        CounterRealmObject counterRealmObject = realm.createObject(CounterRealmObject.class);
        CounterRealmObject counterRealmObjectTemp = recipeRealmObject.getCounterRealmObject();
        counterRealmObject.setAddedToCollection(counterRealmObjectTemp.getAddedToCollection());
        counterRealmObject.setComments(counterRealmObjectTemp.getComments());
        counterRealmObject.setLikes(counterRealmObjectTemp.getLikes());
        counterRealmObject.setMail(counterRealmObjectTemp.getMail());
        counterRealmObject.setPrinted(counterRealmObjectTemp.getPrinted());
        counterRealmObject.setViewed(counterRealmObjectTemp.getViewed());
        return counterRealmObject;
    }

    public void updateIsFavoriteInDB(final boolean checked, final Recipe recipe) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo("_id", recipe.get_id()).findFirst();
                recipeRealmObject.setFavorite(checked);
            }
        });
    }


    public void updateLikes(final Recipe recipe, final long likes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo("_id", recipe.get_id()).findFirst();
                recipeRealmObject.getCounterRealmObject().setLikes(likes);

            }
        });
    }


}
