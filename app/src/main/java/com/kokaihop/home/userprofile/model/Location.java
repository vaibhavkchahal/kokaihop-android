package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Location extends BaseObservable{

    @SerializedName("current")
    private Address current;

    @SerializedName("living")
    private Address living;

    @Bindable
    public Address getLiving() {
        return living;
    }

    public void setLiving(Address living) {
        this.living = living;
        notifyPropertyChanged(BR.living);
    }

    @Bindable
    public Address getCurrent() {
        return current;
    }

    public void setCurrent(Address current) {
        this.current = current;
        notifyPropertyChanged(BR.current);
    }
}
