package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Location{

    @SerializedName("current")
    private Address current;

    @SerializedName("living")
    private Address living;

    public Address getLiving() {
        return living;
    }

    public void setLiving(Address living) {
        this.living = living;
    }

    public Address getCurrent() {
        return current;
    }

    public void setCurrent(Address current) {
        this.current = current;
    }
}
