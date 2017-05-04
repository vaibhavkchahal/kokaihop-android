package com.kokaihop.login;

import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.BaseViewModel;


public class LoginViewModel extends BaseViewModel {

    private String userName = "rajendra.singh@tothenew.com";
    private String password = "kokaihop";

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
        String username = getUserName();
        String password = getPassword();
        setProgressVisible(true);
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
