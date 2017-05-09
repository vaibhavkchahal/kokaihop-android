package com.kokaihop.utility;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class RealmHelper {

    public static Realm realm = null;


    public static Realm getRealmInstance() {
        if (realm == null) {
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().schemaVersion(1).build();
            Realm.setDefaultConfiguration(realmConfiguration);
            // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

            // Create a new empty instance of Realm
            realm = Realm.getDefaultInstance();
        }
        return realm;

    }


}
