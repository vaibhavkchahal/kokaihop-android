package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class CookingStep extends RealmObject {

    private String step;

    private int serialNo;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }
}
