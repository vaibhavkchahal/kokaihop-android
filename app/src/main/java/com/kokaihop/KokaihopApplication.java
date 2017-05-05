package com.kokaihop;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Rajendra Singh on 4/5/17.
 */

public class KokaihopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
