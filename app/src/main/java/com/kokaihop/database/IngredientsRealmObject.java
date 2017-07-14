package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 10/5/17.
 */

public class IngredientsRealmObject extends RealmObject {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("isHeader")
    private boolean isHeader;

    private boolean isServerSyncNeeded;

    @SerializedName("amount")
    private float amount;

    @SerializedName("unit")
    private Unit unit;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @PrimaryKey
    @SerializedName("_id")
    private String _id;
    @SerializedName("dateCreated")
    private String dateCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isServerSyncNeeded() {
        return isServerSyncNeeded;
    }

    public void setServerSyncNeeded(boolean serverSyncNeeded) {
        isServerSyncNeeded = serverSyncNeeded;
    }
}
