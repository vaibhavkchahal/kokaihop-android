package com.kokaihop.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class RecipeCategoryRealmObject extends RealmObject {

    private String _id;
    private String name;
    @PrimaryKey
    private String friendlyUrl;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }


}
