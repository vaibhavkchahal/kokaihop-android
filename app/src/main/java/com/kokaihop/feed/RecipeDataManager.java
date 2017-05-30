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
    private RecipeDataListener recipeDataListener;

    public RecipeDataManager(RecipeDataListener recipeDataListener) {
        this.recipeDataListener = recipeDataListener;
        realm = Realm.getDefaultInstance();

    }

    public RecipeDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<RecipeRealmObject> fetchRecipe(ApiConstants.BadgeType badgeType) {
        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class)
                .equalTo("badgeType", badgeType.value)
                .findAllSorted("badgeDateCreated", Sort.DESCENDING);
        return recipeRealmObjectList;
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

    public interface RecipeDataListener {
        void onTransactionComplete(boolean executed);
    }

    public void updateIsFavoriteInDB(final boolean checked, final RecipeRealmObject recipeRealmObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                recipeRealmObject.setFavorite(checked);
            }
        });
    }


    public void updateLikes(final RecipeRealmObject recipeRealmObject, final long likes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                recipeRealmObject.getCounterRealmObject().setLikes(likes);

            }
        });
    }


}
