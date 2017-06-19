package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class RealmString extends RealmObject {

    private String element;

    public RealmString() {
    }

    public RealmString(String element) {
        this.element = element;
    }

    public String getUserId() {
        return element;
    }

    public void setUserId(String string) {
        this.element = string;
    }

    @Override
    public String toString() {
        return element;
    }
}
