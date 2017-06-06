package com.kokaihop.editprofile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentSettingsBinding;
import com.kokaihop.home.HomeActivity;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

public class SettingsFragment extends Fragment implements View.OnClickListener{


    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSettingsBinding settingsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings,container,false);
        settingsBinding.settingsLogout.setOnClickListener(this);
        settingsBinding.settingsChangePassword.setOnClickListener(this);
        settingsBinding.settingsEmailPreferences.setOnClickListener(this);
        settingsBinding.settingsIvBack.setOnClickListener(this);
        settingsBinding.settingsSave.setOnClickListener(this);
        return settingsBinding.getRoot();
    }

//    Display dialogbox to confirm the user for logout process.
    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
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
        switch (v.getId()){
            case R.id.settings_logout:
                showDialog();
                break;

            case R.id.settings_iv_back:
                getFragmentManager().popBackStack();
                break;

            case R.id.settings_change_password:
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cl_settings_container,changePasswordFragment)
                        .addToBackStack(null)
                        .commit();

        }
    }
}
