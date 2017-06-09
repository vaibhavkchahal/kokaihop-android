package com.kokaihop.editprofile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySettingsBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.home.HomeActivity;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.KokaihopApplication.getContext;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {


    public SettingsActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.settingsLogout.setOnClickListener(this);
        settingsBinding.settingsChangePassword.setOnClickListener(this);
        settingsBinding.settingsEmailPreferences.setOnClickListener(this);
        settingsBinding.settingsEditProfile.setOnClickListener(this);
        settingsBinding.settingsIvBack.setOnClickListener(this);
        settingsBinding.settingsSave.setOnClickListener(this);
    }

    //    Display dialogbox to confirm the user for logout process.
    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        dialog.setTitle("Confirm Logout");
        dialog.setMessage("Do you really want to logout!!!");

        dialog.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN, null);
                new ProfileDataManager().removeData(SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.USER_ID));
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_logout:
                showDialog();
                break;

            case R.id.settings_iv_back:
                finish();
                break;

            case R.id.settings_save:
                finish();
                break;

            case R.id.settings_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.settings_email_preferences:
                startActivity(new Intent(this, EmailPreferencesActivity.class));
                break;

            case R.id.settings_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
        }
    }
}
