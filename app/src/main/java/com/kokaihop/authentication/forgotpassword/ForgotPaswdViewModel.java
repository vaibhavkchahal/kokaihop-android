package com.kokaihop.authentication.forgotpassword;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.BaseViewModel;


public class ForgotPaswdViewModel extends BaseViewModel {

    private String userName;

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.notifyPropertyChanged(BR.userName);
    }


    public void forgot(final View view) {
        final Activity activity = (Activity) view.getContext();
        if (userName.isEmpty() || !AppUtility.isValidEmail(userName)) {
            Toast.makeText(view.getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        setProgressVisible(true);
        new AuthenticationApiHelper(view.getContext()).doForgot(userName, new IApiRequestComplete<ForgotApiResponse>() {
            @Override
            public void onSuccess(ForgotApiResponse response) {
                if (response.isSuccess()) {
                    setProgressVisible(false);
                    Toast.makeText(view.getContext(), R.string.forgot_success_msg, Toast.LENGTH_SHORT).show();
                    activity.finish();

                } else {
                    Toast.makeText(view.getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
