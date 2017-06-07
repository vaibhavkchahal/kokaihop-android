package com.kokaihop.editprofile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;

/**
 * Created by Rajendra Singh on 7/6/17.
 */


public class EmailPreferences extends BaseObservable {
    private boolean todayRecipe;
    private boolean drinkingTips;
    private boolean noEmail;

    @Bindable
    public boolean isTodayRecipe() {
        return todayRecipe;
    }

    public void setTodayRecipe(boolean todayRecipe) {
        this.todayRecipe = todayRecipe;
        notifyPropertyChanged(BR.todayRecipe);
    }

    @Bindable
    public boolean isDrinkingTips() {
        return drinkingTips;
    }

    public void setDrinkingTips(boolean drinkingTips) {
        this.drinkingTips = drinkingTips;
        notifyPropertyChanged(BR.drinkingTips);
    }

    @Bindable
    public boolean isNoEmail() {
        return noEmail;
    }

    public void setNoEmail(boolean noEmail) {
        this.noEmail = noEmail;
        notifyPropertyChanged(BR.noEmail);
    }
}