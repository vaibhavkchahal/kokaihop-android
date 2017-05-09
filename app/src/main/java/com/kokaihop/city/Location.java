package com.kokaihop.city;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class Location implements Parcelable{
        @SerializedName("coordinates")
        float[] coordinates;
        @SerializedName("type")
        private String type;

        protected Location(Parcel in) {
                coordinates = in.createFloatArray();
                type = in.readString();
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
                @Override
                public Location createFromParcel(Parcel in) {
                        return new Location(in);
                }

                @Override
                public Location[] newArray(int size) {
                        return new Location[size];
                }
        };

        @Override
        public int describeContents() {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeFloatArray(coordinates);
                dest.writeString(type);
        }
}
