package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.Unit;

/**
 * Created by Vaibhav Chahal on 13/7/17.
 */

public class IngredientRequestModel {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private float amount;

    @SerializedName("unit")
    private Unit unit;
}


