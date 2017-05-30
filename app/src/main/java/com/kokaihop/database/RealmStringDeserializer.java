package com.kokaihop.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class RealmStringDeserializer implements JsonDeserializer<RealmList<StringObject>> {

    @Override
    public RealmList<StringObject> deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {

        RealmList<StringObject> realmStrings = new RealmList<>();
        JsonArray stringList = json.getAsJsonArray();

        for (JsonElement stringElement : stringList) {
            realmStrings.add(new StringObject(stringElement.getAsString()));
        }

        return realmStrings;
    }
}