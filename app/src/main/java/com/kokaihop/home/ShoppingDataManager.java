package com.kokaihop.home;

import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.utility.Constants;

import io.realm.Realm;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class ShoppingDataManager {
    private Realm realm;

    public ShoppingDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public ShoppingListRealmObject fetchShoppingRealmObject() {
        //return the managed object
        ShoppingListRealmObject realmObject = realm.where(ShoppingListRealmObject.class)
                .equalTo(Constants.SHOPPING_LIST_NAME_KEY, Constants.SHOPPING_LIST_NAME_VALUE).findFirst();
        return realmObject;
    }

    public void insertOrUpdateData(ShoppingListRealmObject object) {
        realm.beginTransaction();
        realm.insertOrUpdate(object);
        realm.commitTransaction();
    }
}


