package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class RealmString extends RealmObject {

    private String string;

    public RealmString() {
    }

    public RealmString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }


}
