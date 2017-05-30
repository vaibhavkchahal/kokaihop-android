package com.kokaihop.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class StringObject extends RealmObject {

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @PrimaryKey
    private String string;
}
