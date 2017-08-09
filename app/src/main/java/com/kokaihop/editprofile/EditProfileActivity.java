package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEditProfileBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.city.CityDetails;
import com.kokaihop.userprofile.ConfirmImageUploadActivity;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.Logger;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;

public class EditProfileActivity extends BaseActivity {

    private static int CONFIRM_REQUEST_CODE = 51;
    private EditProfileViewModel editProfileViewModel;
    private Uri imageUri = null;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditProfileBinding editProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        editProfileViewModel = new EditProfileViewModel(this, editProfileBinding);
        editProfileBinding.setViewModel(editProfileViewModel);
        editProfileBinding.setUser(User.getInstance());
        editProfileBinding.executePendingBindings();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.edit_profile_screen));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY || requestCode == EditProfileViewModel.REQUEST_CAMERA) {
                if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                    imageUri = data.getData();
                    filePath = CameraUtils.getRealPathFromURI(EditProfileActivity.this, imageUri);
                    Intent confirmIntent = new Intent(this, ConfirmImageUploadActivity.class);
                    confirmIntent.setData(imageUri);
                    startActivityForResult(confirmIntent, CONFIRM_REQUEST_CODE);
                } else {
                    filePath = CameraUtils.onCaptureImageResult();
                    editProfileViewModel.uploadImageOnCloudinary(filePath);
                }
                Logger.d("File Path", filePath);
            } else if (requestCode == EditProfileViewModel.REQUEST_CITY) {
                CityDetails citySelected = data.getParcelableExtra("citySelected");

                editProfileViewModel.getCity().getLiving().setCountryCode(citySelected.getCountryCode());
                editProfileViewModel.getCity().getLiving().setGeoId(citySelected.getGeoId());
                editProfileViewModel.getCity().getLiving().setCountryCode(citySelected.getCountryCode());
                editProfileViewModel.getCity().getLiving().setLoc(citySelected.getLoc());
                editProfileViewModel.getCity().getLiving().setName(citySelected.getName());
                editProfileViewModel.setCityName(citySelected.getName());
            } else if (requestCode == CONFIRM_REQUEST_CODE) {
                editProfileViewModel.uploadImageOnCloudinary(filePath);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.userChoosenTask.equals(getString(R.string.take_photo)))
                        CameraUtils.cameraIntent(this);
                    else if (CameraUtils.userChoosenTask.equals(getString(R.string.choose_from_library)))
                        CameraUtils.galleryIntent(this);
                }
                break;
        }
    }

    public void selectImage() {
        CameraUtils.selectImage(this);
    }
}
