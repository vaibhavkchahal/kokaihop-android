package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserName extends BaseObservable{

    @SerializedName("first")
    private String first;

    @SerializedName("last")
    private String last;

    @SerializedName("full")
    private String full;

    @Bindable
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
        notifyPropertyChanged(BR.first);
    }

    @Bindable
    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
        notifyPropertyChanged(BR.last);
    }

    @Bindable
    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
        notifyPropertyChanged(BR.full);
    }
}
