package com.kokaihop.base;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */

public class BaseViewModel extends BaseObservable {

    private boolean isProgressVisible = false;

    @Bindable
    public boolean isProgressVisible() {
        return isProgressVisible;
    }

    public void setProgressVisible(boolean progressVisible) {
        isProgressVisible = progressVisible;
        this.notifyPropertyChanged(BR.progressVisible);
    }

}
