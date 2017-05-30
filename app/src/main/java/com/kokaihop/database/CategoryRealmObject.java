package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 10/5/17.
 */

public class CategoryRealmObject extends RealmObject {

   @PrimaryKey @SerializedName("id")
    private String id;
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("name")
    private String name;
}
