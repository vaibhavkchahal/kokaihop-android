package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityChangePasswordBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 6/6/17.
 */

public class ChangePasswordViewModel extends BaseViewModel {

    Context context;
    ActivityChangePasswordBinding changePasswordBinding;

    public ChangePasswordViewModel(Context context, ActivityChangePasswordBinding changePasswordBinding) {
        this.context = context;
        this.changePasswordBinding = changePasswordBinding;
    }

    public void changePassword() {
        String newPassword = changePasswordBinding.etNewPassword.getText().toString();
        if (!validatePassword(newPassword)) {
            Toast.makeText(context, context.getString(R.string.password_validation_msg), Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() > newPassword.trim().length()) {
            Toast.makeText(context, context.getString(R.string.passsword_warning_space_start_end), Toast.LENGTH_SHORT).show();
        } else {
            setProgressVisible(true);
            if (newPassword.equals(changePasswordBinding.etConfirmPassword.getText().toString())) {
                String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
                UserPassword userPassword = new UserPassword(newPassword);
                new SettingsApiHelper().changePassword(accessToken, userId, userPassword, new IApiRequestComplete<SettingsResponse>() {
                    @Override
                    public void onSuccess(SettingsResponse response) {
                        setProgressVisible(false);
                        if (response.isSuccess()) {
                            Toast.makeText(context, context.getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        } else {
                            Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                        Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SettingsResponse response) {
                        setProgressVisible(false);
                        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, context.getString(R.string.password_not_confirmed_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validatePassword(String password) {
        if (AppUtility.isEmptyString(password) || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }
}
