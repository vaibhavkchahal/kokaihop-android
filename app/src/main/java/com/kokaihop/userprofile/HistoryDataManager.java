package com.kokaihop.userprofile;

import com.kokaihop.database.RecipeHistoryRealmObject;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 14/6/17.
 */

public class HistoryDataManager {

    private Realm realm;

    public HistoryDataManager() {
        this.realm = Realm.getDefaultInstance();
    }

    public void updateHistory(String recipeId){
        realm.beginTransaction();
        realm.insertOrUpdate(new RecipeHistoryRealmObject(recipeId, new Date().getTime()));
        realm.commitTransaction();
    }

    public ArrayList<RecipeHistoryRealmObject> getHistory(){
        RealmResults<RecipeHistoryRealmObject> realmResult = realm.where(RecipeHistoryRealmObject.class).findAll().sort("timeStamp", Sort.DESCENDING);
        ArrayList<RecipeHistoryRealmObject> historyRealmObjects = (ArrayList<RecipeHistoryRealmObject>) realm.copyFromRealm(realmResult);
        return historyRealmObjects;
    }
}
