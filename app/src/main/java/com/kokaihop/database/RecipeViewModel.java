package com.kokaihop.database;

import android.databinding.BaseObservable;

/**
 * Created by Vaibhav Chahal on 22/5/17.
 */

public class RecipeViewModel extends BaseObservable{

    private String isFavorite;

    public RecipeViewModel(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}
