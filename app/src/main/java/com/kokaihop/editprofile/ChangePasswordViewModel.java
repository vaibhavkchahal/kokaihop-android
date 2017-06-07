package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityChangePasswordBinding;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 6/6/17.
 */

public class ChangePasswordViewModel {

    Context context;
    ActivityChangePasswordBinding changePasswordBinding;

    public ChangePasswordViewModel(Context context, ActivityChangePasswordBinding changePasswordBinding) {
        this.context = context;
        this.changePasswordBinding = changePasswordBinding;
    }

    public void changePassword() {
        String newPassword = changePasswordBinding.etNewPassword.getText().toString();
        if (validatePassword(newPassword)) {

            if (newPassword.equals(changePasswordBinding.etConfirmPassword.getText().toString())) {
                String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
                UserPassword userPassword = new UserPassword(newPassword);
                new EditProfileApiHelper().changePassword(accessToken, userId, userPassword, new IApiRequestComplete<EditProfileResponse>() {
                    @Override
                    public void onSuccess(EditProfileResponse response) {
                        if (response.isSuccess()) {
                            Toast.makeText(context, R.string.password_updated, Toast.LENGTH_SHORT).show();
                            ((Activity)context).finish();
                        } else {
                            Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(EditProfileResponse response) {
                        Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, R.string.password_not_confirmed_msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void backToSettings() {
        ((Activity)context).finish();
    }

    public boolean validatePassword(String password) {
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        return true;
    }
}
