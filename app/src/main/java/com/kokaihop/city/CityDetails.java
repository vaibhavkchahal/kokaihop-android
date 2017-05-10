package com.kokaihop.city;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class CityDetails implements Parcelable{

    @SerializedName("_id")
    private String id;
    @SerializedName("geoId")
    private int geoId;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private float latitude;
    @SerializedName("longitude")
    private float longitude;
    @SerializedName("featureClass")
    private char featureClass;
    @SerializedName("featureCode")
    private String featureCode;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("cc2")
    private String cc2;
    @SerializedName("admin1code")
    private String admin1Code;
    @SerializedName("admin2code")
    private String admin2Code;
    @SerializedName("admin3code")
    private String admin3Code;
    @SerializedName("admin4code")
    private String admin4Code;
    @SerializedName("population")
    private int population;
    @SerializedName("elevation")
    private int elevation;
    @SerializedName("dem")
    private int dem;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("modificationDate")
    private String modificationDate;
    @SerializedName("isUpdated")
    private boolean isUpdated;
    @SerializedName("loc")
    private Location loc;
    @SerializedName("__v")
    private String __v;
    @SerializedName("alternateNames")
    private AlternateNames alternateNames;

    protected CityDetails(Parcel in) {
        id = in.readString();
        geoId = in.readInt();
        name = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        featureCode = in.readString();
        countryCode = in.readString();
        cc2 = in.readString();
        admin1Code = in.readString();
        admin2Code = in.readString();
        admin3Code = in.readString();
        admin4Code = in.readString();
        population = in.readInt();
        elevation = in.readInt();
        dem = in.readInt();
        timezone = in.readString();
        modificationDate = in.readString();
        isUpdated = in.readByte() != 0;
        loc = in.readParcelable(Location.class.getClassLoader());
        __v = in.readString();
        alternateNames = in.readParcelable(AlternateNames.class.getClassLoader());
    }

    public static final Creator<CityDetails> CREATOR = new Creator<CityDetails>() {
        @Override
        public CityDetails createFromParcel(Parcel in) {
            return new CityDetails(in);
        }

        @Override
        public CityDetails[] newArray(int size) {
            return new CityDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public char getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(char featureClass) {
        this.featureClass = featureClass;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCc2() {
        return cc2;
    }

    public void setCc2(String cc2) {
        this.cc2 = cc2;
    }

    public String getAdmin1Code() {
        return admin1Code;
    }

    public void setAdmin1Code(String admin1Code) {
        this.admin1Code = admin1Code;
    }

    public String getAdmin2Code() {
        return admin2Code;
    }

    public void setAdmin2Code(String admin2Code) {
        this.admin2Code = admin2Code;
    }

    public String getAdmin3Code() {
        return admin3Code;
    }

    public void setAdmin3Code(String admin3Code) {
        this.admin3Code = admin3Code;
    }

    public String getAdmin4Code() {
        return admin4Code;
    }

    public void setAdmin4Code(String admin4Code) {
        this.admin4Code = admin4Code;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getDem() {
        return dem;
    }

    public void setDem(int dem) {
        this.dem = dem;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public AlternateNames getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(AlternateNames alternateNames) {
        this.alternateNames = alternateNames;
    }

    @Override
    public String toString() {
        return "CityDetails{" +
                "id='" + id + '\'' +
                ", geoId=" + geoId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", featureClass=" + featureClass +
                ", featureCode='" + featureCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", cc2='" + cc2 + '\'' +
                ", admin1Code='" + admin1Code + '\'' +
                ", admin2Code='" + admin2Code + '\'' +
                ", admin3Code='" + admin3Code + '\'' +
                ", admin4Code='" + admin4Code + '\'' +
                ", population=" + population +
                ", elevation=" + elevation +
                ", dem=" + dem +
                ", timezone='" + timezone + '\'' +
                ", modificationDate='" + modificationDate + '\'' +
                ", isUpdated=" + isUpdated +
                ", loc=" + loc +
                ", __v='" + __v + '\'' +
                ", alternateNames=" + alternateNames +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(geoId);
        dest.writeString(name);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(featureCode);
        dest.writeString(countryCode);
        dest.writeString(cc2);
        dest.writeString(admin1Code);
        dest.writeString(admin2Code);
        dest.writeString(admin3Code);
        dest.writeString(admin4Code);
        dest.writeInt(population);
        dest.writeInt(elevation);
        dest.writeInt(dem);
        dest.writeString(timezone);
        dest.writeString(modificationDate);
        dest.writeByte((byte) (isUpdated ? 1 : 0));
        dest.writeParcelable(loc, flags);
        dest.writeString(__v);
        dest.writeParcelable(alternateNames, flags);
    }
}
