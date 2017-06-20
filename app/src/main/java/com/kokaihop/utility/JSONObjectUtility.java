package com.kokaihop.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rajendra Singh on 16/6/17.
 */

public class JSONObjectUtility {

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

}
