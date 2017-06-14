package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEditProfileBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.city.CityDetails;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.Logger;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding editProfileBinding;
    private EditProfileViewModel editProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        editProfileViewModel = new EditProfileViewModel(this, editProfileBinding);
        editProfileBinding.setViewModel(editProfileViewModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        String filePath;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY || requestCode == EditProfileViewModel.REQUEST_CAMERA) {
                if (requestCode == EditProfileViewModel.REQUEST_GALLERY) {
                    imageUri = data.getData();
                    filePath = CameraUtils.getRealPathFromURI(EditProfileActivity.this, imageUri);
                } else {
                    filePath = CameraUtils.onCaptureImageResult();

                }
                Logger.d("File Path", filePath);

                //TODO : cloudinary image upload code goes here
                editProfileViewModel.uploadImageOnCloudinary(filePath);

            } else if (requestCode == EditProfileViewModel.REQUEST_CITY) {
                CityDetails citySelected = data.getParcelableExtra("citySelected");

                editProfileViewModel.getCity().getLiving().setCountryCode(citySelected.getCountryCode());
                editProfileViewModel.getCity().getLiving().setGeoId(citySelected.getGeoId());
                editProfileViewModel.getCity().getLiving().setCountryCode(citySelected.getCountryCode());
                editProfileViewModel.getCity().getLiving().setLoc(citySelected.getLoc());
                editProfileViewModel.getCity().getLiving().setName(citySelected.getName());
                editProfileViewModel.setCityName(citySelected.getName());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
