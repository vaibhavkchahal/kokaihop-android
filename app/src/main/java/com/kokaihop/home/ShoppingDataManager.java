package com.kokaihop.home;

import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.database.Unit;
import com.kokaihop.utility.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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

    public List<IngredientsRealmObject> fetchCopyIngredientRealmObjects(RealmList<IngredientsRealmObject> objects) {
        //return the unmanaged object
        return realm.copyFromRealm(objects);
    }

    public void insertOrUpdateData(final ShoppingListRealmObject object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }

    public void updateShoppingIngredientList(final List<Unit> unitList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(unitList);
            }
        });
    }

    public List<Unit> getIngredientUnits() {
        RealmResults<Unit> unitRealmResults = realm.where(Unit.class).findAll();
        return realm.copyFromRealm(unitRealmResults);
    }

    public void addIngredientObjectToList(final IngredientsRealmObject ingredientsRealmObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(ingredientsRealmObject);
                ShoppingListRealmObject realmObject = realm.where(ShoppingListRealmObject.class)
                        .equalTo(Constants.SHOPPING_LIST_NAME_KEY, Constants.SHOPPING_LIST_NAME_VALUE).findFirst();
                realmObject.getIngredients().add(0, ingredientsRealmObject);
            }
        });
    }
}


