package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 27/7/17.
 */

public class DeactivatedRecipeResponse {

    @SerializedName("recipes")
    private ArrayList<DeactivatedRecipe> deactivatedRecipes;

    @SerializedName("count")
    private int count;

    public ArrayList<DeactivatedRecipe> getDeactivatedRecipes() {
        return deactivatedRecipes;
    }

    public void setDeactivatedRecipes(ArrayList<DeactivatedRecipe> deactivatedRecipes) {
        this.deactivatedRecipes = deactivatedRecipes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    class DeactivatedRecipe {
        @SerializedName("_id")
        private String _id;

        @SerializedName("friendlyUrl")
        private String friendlyUrl;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getFriendlyUrl() {
            return friendlyUrl;
        }

        public void setFriendlyUrl(String friendlyUrl) {
            this.friendlyUrl = friendlyUrl;
        }
    }
}
