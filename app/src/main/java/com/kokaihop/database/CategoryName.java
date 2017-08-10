package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 10/8/17.
 */

public class CategoryName extends RealmObject {

    @SerializedName("en")
    private String en;

    @SerializedName("sv")
    private String sv;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getSv() {
        return sv;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }
}