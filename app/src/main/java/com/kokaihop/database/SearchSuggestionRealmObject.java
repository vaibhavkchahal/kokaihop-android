package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 22/6/17.
 */

public class SearchSuggestionRealmObject extends RealmObject {

    private String keyword;
    private long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
