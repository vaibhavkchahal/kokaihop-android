package com.kokaihop.editprofile;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.userprofile.model.Settings;

/**
 * Created by Rajendra Singh on 7/6/17.
 */


public class EmailPreferences extends BaseObservable {
    @SerializedName("settings")
    private Settings settings;

    public Settings getSettings() {
        if(settings==null)
            settings = new Settings();
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}