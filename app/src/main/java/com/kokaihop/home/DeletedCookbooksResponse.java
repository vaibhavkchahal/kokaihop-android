package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 27/7/17.
 */

public class DeletedCookbooksResponse {

    @SerializedName("recipeCollections")
    private ArrayList<DeletedCookbooks> deletedCookbooks;

    public ArrayList<DeletedCookbooks> getDeletedCookbooks() {
        return deletedCookbooks;
    }

    public void setDeletedCookbooks(ArrayList<DeletedCookbooks> deletedCookbooks) {
        this.deletedCookbooks = deletedCookbooks;
    }

    class DeletedCookbooks {
        @SerializedName("_id")
        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }
}
