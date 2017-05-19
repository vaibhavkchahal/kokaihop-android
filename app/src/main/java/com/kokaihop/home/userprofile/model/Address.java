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
    Loc loc;

    @Bindable
    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
        notifyPropertyChanged(BR.loc);
    }
}
