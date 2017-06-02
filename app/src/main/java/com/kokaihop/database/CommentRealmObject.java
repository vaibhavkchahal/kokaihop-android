package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rajendra Singh on 1/6/17.
 */


public class CommentRealmObject extends RealmObject {

    @PrimaryKey @SerializedName("_id")
    private String _id;
    @SerializedName("name")
    private String name;
    @SerializedName("sourceUser")
    private UserRealmObject sourceUser;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRealmObject getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(UserRealmObject sourceUser) {
        this.sourceUser = sourceUser;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    @SerializedName("dateCreated")
    private  long dateCreated;



  /*  {
        "_id": "563880136561d10300aa03c8",
            "name": "RECIPE_COMMENT",
            "sourceUser": {
        "id": "56387a9d1e443c0300c547ad",
                "oldId": 26883912,
                "friendlyUrl": "lennart46"
    },
        "payload": {
        "recipe": {
            "id": "56387b241a258f0300c46d88",
                    "oldId": 1546001
        },
        "comment": {
            "content": "Ett till jättebra recept från dej.Tack GOE"
        },
        "replyCount": 0,
                "replyEvents": []
    },
        "dateCreated": 1413728779540
    },*/
}
