package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 18/5/17.
 */

public class Rating extends RealmObject {

    @SerializedName("average")
    private float average;
    @SerializedName("raters")
    private int raters;

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getRaters() {
        return raters;
    }

    public void setRaters(int raters) {
        this.raters = raters;
    }
}
