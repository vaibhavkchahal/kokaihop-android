package com.kokaihop.cookbooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 5/7/17.
 */

public class RenameCookbookRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("_id")
    private String _id;

    public RenameCookbookRequest(String name, String _id) {
        this.name = name;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
