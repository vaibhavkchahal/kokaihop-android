package com.kokaihop.home;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kokaihop.database.IngredientsRealmObject;

import java.lang.reflect.Type;

/**
 * Created by Vaibhav Chahal on 14/7/17.
 */

public class IngredientSerializer implements JsonSerializer<IngredientsRealmObject> {


    @Override
    public JsonElement serialize(IngredientsRealmObject object, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jObj = (JsonObject) new GsonBuilder().create().toJsonTree(object);
        jObj.remove("_id");
        return jObj;
    }

}
