package com.kokaihop.city;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class Loc implements Parcelable{
        @SerializedName("coordinates")
        private float[] coordinates;
        @SerializedName("type")
        private String type;

        protected Loc(Parcel in) {
                coordinates = in.createFloatArray();
                type = in.readString();
        }

        public static final Creator<Loc> CREATOR = new Creator<Loc>() {
                @Override
                public Loc createFromParcel(Parcel in) {
                        return new Loc(in);
                }

                @Override
                public Loc[] newArray(int size) {
                        return new Loc[size];
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

        public float[] getCoordinates() {
                return coordinates;
        }

        public void setCoordinates(float[] coordinates) {
                this.coordinates = coordinates;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        @Override
        public String toString() {
                return "Loc{" +
                        "coordinates=" + Arrays.toString(coordinates) +
                        ", type='" + type + '\'' +
                        '}';
        }
}
