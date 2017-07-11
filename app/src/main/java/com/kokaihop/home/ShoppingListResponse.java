package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.ShoppingListRealmObject;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */
public class ShoppingListResponse {

    @SerializedName("list")
    private ShoppingListRealmObject shoppingListRealmObject;

    public ShoppingListRealmObject getShoppingListRealmObject() {
        return shoppingListRealmObject;
    }

    public void setShoppingListRealmObject(ShoppingListRealmObject shoppingListRealmObject) {
        this.shoppingListRealmObject = shoppingListRealmObject;
    }
}
