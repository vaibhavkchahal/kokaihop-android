package com.kokaihop.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.altaworks.kokaihop.ui.BR;


public class LoginViewModel extends BaseObservable {

    private String userName;
    private String password;
    private boolean isProgressVisible = false;

    @Bindable
    public boolean isProgressVisible() {
        return isProgressVisible;
    }

    public void setProgressVisible(boolean progressVisible) {
        isProgressVisible = progressVisible;
        this.notifyPropertyChanged(BR.progressVisible);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.notifyPropertyChanged(BR.password);
    }

    public void login(View view) {
        String username=getUserName();
        String password=getPassword();

        setProgressVisible(true);

        // make login call with username and password

    }

}
