package com.kokaihop.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.network.IApiRequestComplete;


public class LoginViewModel extends BaseObservable {

    private String userName = "rajendra.singh@tothenew.com";
    private String password = "kokaihop";

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

    public void login(final View view) {
        String username=getUserName();
        String password=getPassword();

        setProgressVisible(true);

        // make login call with username and password

        new LoginApiHelper(view.getContext()).doLogin(username, password, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
