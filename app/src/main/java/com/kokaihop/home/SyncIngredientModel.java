package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.IngredientsRealmObject;

import java.util.List;

/**
 * Created by Vaibhav Chahal on 13/7/17.
 */

public class SyncIngredientModel {

    @SerializedName("items")
    private List<IngredientsRealmObject> objects;

    public List<IngredientsRealmObject> getRealmObjects() {
        return objects;
    }

    public void setList(List<IngredientsRealmObject> realmObjects) {
        this.objects = realmObjects;
    }
}