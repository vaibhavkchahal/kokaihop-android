/*
package com.kokaihop.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import io.realm.RealmList;

*/
/**
 * Created by Rajendra Singh on 30/5/17.
 *//*


public class RealmStringDeserializer implements JsonDeserializer<RealmList<RealmString>> {

@Override
public RealmList<RealmString> deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context) throws JsonParseException {

        RealmList<RealmString> realmStrings = new RealmList<>();
        JsonArray stringList = json.getAsJsonArray();

        for (JsonElement stringElement : stringList) {
        realmStrings.add(new RealmString(stringElement.getAsString()));
        }

        return realmStrings;
        }
        }
*/
