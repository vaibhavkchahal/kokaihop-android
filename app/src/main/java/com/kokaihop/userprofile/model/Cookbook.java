package com.kokaihop.userprofile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.database.RecipeRealmObject;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class Cookbook extends BaseObservable {

    private String _id;
    private String name;
    private String friendlyUrl;
    private ArrayList<RecipeRealmObject> recipes;
    private String mainImageUrl;
    private int total;
    private boolean contains;

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

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public ArrayList<RecipeRealmObject> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<RecipeRealmObject> recipes) {
        this.recipes = recipes;
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    @Bindable
    public boolean isContains() {
        return contains;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
        notifyPropertyChanged(BR.contains);
    }
}
