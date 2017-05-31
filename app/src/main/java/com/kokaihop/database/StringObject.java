package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class StringObject extends RealmObject {

    private String string;

    public StringObject() {
    }

    public StringObject(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }


}
