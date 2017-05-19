package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Loc extends BaseObservable{

    @SerializedName("coordinates")
    private int[] coordinates;

    @SerializedName("type")
    private String type;

    @Bindable
    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
        notifyPropertyChanged(BR.coordinates);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.coordinates);
    }
}
