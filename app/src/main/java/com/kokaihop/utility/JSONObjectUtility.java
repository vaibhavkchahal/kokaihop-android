package com.kokaihop.utility;

import com.google.gson.Gson;
import com.kokaihop.database.RealmString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rajendra Singh on 16/6/17.
 */

public class JSONObjectUtility {

    Gson gson = new Gson();

    //    update the cooking step object of the recipe.
    public JSONObject updateCookingStepsInRecipe(final JSONObject jsonObject) {
        try {
            if (jsonObject.has("cookingSteps")) {
                JSONArray JSONArray = jsonObject.getJSONArray("cookingSteps");
                JSONArray updatedJSONArray = new JSONArray();
                for (int i = 0; i < JSONArray.length(); i++) {
                    String step = JSONArray.getString(i);
                    JSONObject stepJSONObject = new JSONObject();
                    stepJSONObject.put("step", step);
                    stepJSONObject.put("serialNo", i + 1);
                    updatedJSONArray.put(stepJSONObject);
                }
                jsonObject.remove("cookingSteps");
                jsonObject.put("cookingSteps", updatedJSONArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //    remove a particular key from the JSON data
    public JSONObject removeKeyFromJSON(final JSONObject jsonObject, String key) {
        jsonObject.remove(key);
        return jsonObject;
    }

    //    remove a particular key from the JSON data
    public JSONObject changeKeyOfJSON(final JSONObject jsonObject, String oldKey, String newKey) {
        try {
            jsonObject.put(newKey, jsonObject.get(oldKey));
//            jsonObject.remove(oldKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject convertStringArrayToRealmStringArray(JSONObject jsonObject, String key) {
        JSONArray jsonArray;
        JSONArray jsonFinalArray = new JSONArray();
        try {
            jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("element", new RealmString(jsonArray.getString(i)));
                jsonFinalArray.put(jsonObj);
            }
            jsonObject.remove(key);
            jsonObject.put(key, jsonFinalArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.e("JSON Changed", jsonObject.toString());
        return jsonObject;
    }
}
