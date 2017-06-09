package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEditProfileBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.CameraUtils;

import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EditProfileViewModel.REQUEST_GALLERY)
                CameraUtils.onSelectFromGalleryResult(data, editProfileBinding.ivUserProfilePic);
            else if (requestCode == EditProfileViewModel.REQUEST_CAMERA)
                CameraUtils.onCaptureImageResult(data,editProfileBinding.ivUserProfilePic);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.userChoosenTask.equals(getString(R.string.take_photo)))
                        CameraUtils.cameraIntent(this);
                    else if (CameraUtils.userChoosenTask.equals(getString(R.string.choose_from_library)))
                        CameraUtils.galleryIntent(this);
                } else {
                }
                break;
        }
    }

    public void selectImage() {
        CameraUtils.selectImage(this);
    }
}
