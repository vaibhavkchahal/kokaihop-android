package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vaibhav Chahal on 13/7/17.
 */

public class SyncIngredientDeletionModel {

    @SerializedName("ids")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
