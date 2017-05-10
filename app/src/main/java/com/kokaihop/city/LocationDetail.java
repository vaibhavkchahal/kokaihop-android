package com.kokaihop.city;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class LocationDetail implements Parcelable{
        @SerializedName("coordinates")
        float[] coordinates;
        @SerializedName("type")
        private String type;

        protected LocationDetail(Parcel in) {
                coordinates = in.createFloatArray();
                type = in.readString();
        }

        public static final Creator<LocationDetail> CREATOR = new Creator<LocationDetail>() {
                @Override
                public LocationDetail createFromParcel(Parcel in) {
                        return new LocationDetail(in);
                }

                @Override
                public LocationDetail[] newArray(int size) {
                        return new LocationDetail[size];
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
