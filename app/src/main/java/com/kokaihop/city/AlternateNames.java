package com.kokaihop.city;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class AlternateNames implements Parcelable{
    @SerializedName("link")
    private String link;

    protected AlternateNames(Parcel in) {
        link = in.readString();
    }

    public static final Creator<AlternateNames> CREATOR = new Creator<AlternateNames>() {
        @Override
        public AlternateNames createFromParcel(Parcel in) {
            return new AlternateNames(in);
        }

        @Override
        public AlternateNames[] newArray(int size) {
            return new AlternateNames[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
    }
}
