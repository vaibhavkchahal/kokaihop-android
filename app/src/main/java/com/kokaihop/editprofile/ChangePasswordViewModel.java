package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentChangePasswordBinding;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 6/6/17.
 */

public class ChangePasswordViewModel {

    Context context;
    FragmentChangePasswordBinding changePasswordBinding;
    Fragment fragment;

    public ChangePasswordViewModel(Fragment fragment, FragmentChangePasswordBinding changePasswordBinding) {
        this.context = fragment.getContext();
        this.fragment = fragment;
        this.changePasswordBinding = changePasswordBinding;
    }

    public void changePassword() {
        String newPassword = changePasswordBinding.etNewPassword.getText().toString();
        if (validatePassword(newPassword)) {

            if (newPassword.equals(changePasswordBinding.etConfirmPassword.getText().toString())) {
                Toast.makeText(context, "Password!!!", Toast.LENGTH_SHORT).show();
                String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
                UserPassword userPassword = new UserPassword(newPassword);
                new EditProfileApiHelper().changePassword(accessToken, userId, userPassword, new IApiRequestComplete<ChangePasswordResponse>() {
                    @Override
                    public void onSuccess(ChangePasswordResponse response) {
                        if (response.isSuccess()) {
                            Toast.makeText(context, R.string.password_updated, Toast.LENGTH_SHORT).show();
                            fragment.getFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ChangePasswordResponse response) {
                        Toast.makeText(context, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                    }
                });
                ((Activity) context).getFragmentManager().popBackStack();
            } else {
                Toast.makeText(context, R.string.password_not_confirmed_msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void backToSettings() {
        fragment.getFragmentManager().popBackStack();
    }

    public boolean validatePassword(String password) {
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        return true;
    }

}
