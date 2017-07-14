package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.Unit;

import java.util.List;

/**
 * Created by Vaibhav Chahal on 12/7/17.
 */

public class ShoppingUnitResponse {

    @SerializedName("units")
    List<Unit> units;

    public List<Unit> getUnits() {
        return units;
    }
}
