package com.kokaihop.cookbooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 29/6/17.
 */

public class CookbookName {
    @SerializedName("name")
    String name;

    public CookbookName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
