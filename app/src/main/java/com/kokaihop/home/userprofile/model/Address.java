package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Address extends BaseObservable{

    @SerializedName("loc")
    private Loc loc;
    @SerializedName("geoId")
    private int geoId;
    @SerializedName("name")
    private String name;
    @SerializedName("countryCode")
    private String countryCode;

    public int getGeoId() {
        return geoId;
    }

    public void setGeoId(int geoId) {
        this.geoId = geoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Bindable
    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
        notifyPropertyChanged(BR.loc);
    }
}
