package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 18/5/17.
 */

public class Rating extends RealmObject {

    @SerializedName("average")
    private double average;
    @SerializedName("raters")
    private long raters;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public long getRaters() {
        return raters;
    }

    public void setRaters(long raters) {
        this.raters = raters;
    }
}
