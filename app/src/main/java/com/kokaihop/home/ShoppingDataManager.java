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

    private static final String INGREDIENT_ID = "_id";
    private static final String UNIT_ID = "id";


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

    public void updateIngredientObject(final String id, final String name, final float amount, final String unitName, final String unitId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                IngredientsRealmObject ingredientsRealmObject = realm.where(IngredientsRealmObject.class)
                        .equalTo(INGREDIENT_ID, id).findFirst();
                ingredientsRealmObject.setName(name);
                ingredientsRealmObject.setAmount(amount);
                Unit unitRealmObject = realm.where(Unit.class)
                        .equalTo(UNIT_ID, unitId).findFirst();
                unitRealmObject.setName(unitName);
//                realm.insertOrUpdate(unitRealmObject);
                ingredientsRealmObject.setUnit(unitRealmObject);
                ingredientsRealmObject.setServerSyncNeeded(true);
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
                realmObject.getIngredients().add(ingredientsRealmObject);
            }
        });
    }

    public void removeIngredientFromRealmDatabase(final List<IngredientsRealmObject> realmObjects) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (IngredientsRealmObject object : realmObjects) {
                    if (object.isServerSyncNeeded()) {
                        IngredientsRealmObject ingredientsRealmObject = realm.where(IngredientsRealmObject.class)
                                .equalTo(INGREDIENT_ID, object.get_id()).findFirst();
                        ingredientsRealmObject.deleteFromRealm();
                    }
                }
            }
        });
    }

    public void updateIngredientDeleteFlag(final IngredientsRealmObject realmObject, final boolean isDelete) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                IngredientsRealmObject ingredientsRealmObject = realm.where(IngredientsRealmObject.class)
                        .equalTo(INGREDIENT_ID, realmObject.get_id()).findFirst();
                ingredientsRealmObject.setIngredientDelete(isDelete);
            }
        });
    }
}


