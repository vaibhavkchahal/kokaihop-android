package com.kokaihop.home;

import android.app.Activity;
import android.content.Context;

import com.kokaihop.base.BaseViewModel;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */
public class AddIngredientViewModel extends BaseViewModel {

    public AddIngredientViewModel() {
    }

    @Override
    protected void destroy() {
    }

    public void onBackPressed(Context context) {
        ((Activity) context).finish();
    }

}
