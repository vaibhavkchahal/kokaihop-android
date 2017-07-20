package com.kokaihop.cookbooks.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;


/**
 * Created by Rajendra Singh on 6/7/17.
 */

public class ItemEditor extends BaseObservable {

    private boolean editMode;

    @Bindable
    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyPropertyChanged(BR.editMode);
    }
}
