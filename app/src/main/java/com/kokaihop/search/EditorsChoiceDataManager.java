package com.kokaihop.search;

import com.kokaihop.database.EditorsChoiceRealmObject;

import org.json.JSONObject;

import io.realm.Realm;

/**
 * Created by Rajendra Singh on 9/8/17.
 */

public class EditorsChoiceDataManager {

    Realm realm;

    public EditorsChoiceDataManager() {
        this.realm = Realm.getDefaultInstance();
    }

    public void updateEditorsChoice(JSONObject editorsChoiceData) {
        realm.beginTransaction();
        realm.createOrUpdateObjectFromJson(EditorsChoiceRealmObject.class, editorsChoiceData);
        realm.commitTransaction();
    }

    public EditorsChoiceRealmObject getEditorChoice(String id) {
        return realm.where(EditorsChoiceRealmObject.class).equalTo("_id", id).findFirst();
    }
}
