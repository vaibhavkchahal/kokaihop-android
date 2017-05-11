package com.kokaihop.utility;

import com.kokaihop.database.DBConstants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.kokaihop.database.DBConstants.DATABASE_NAME;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class RealmHelper {

    public static Realm realm = null;


    public static Realm getRealmInstance() {
        if (realm == null) {
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(DATABASE_NAME).
                    schemaVersion(DBConstants.SCHEMA_VERSION).build();
            Realm.setDefaultConfiguration(realmConfiguration);
            // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

            // Create a new empty instance of Realm
            realm = Realm.getDefaultInstance();
        }
        return realm;

    }
}
