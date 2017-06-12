package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.databinding.ActivityEditProfileBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.city.CityActivity;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CloudinaryDetail;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.UploadImageAsync;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajendra Singh on 7/6/17.
 */

public class EditProfileViewModel extends BaseViewModel {
    public static final int REQUEST_CITY = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_CAMERA = 3;
    public static final int MY_PERMISSIONS = 4;
    private Context context;
    private ActivityEditProfileBinding editProfileBinding;
    private String email, city, profileImageUrl;
    User user;

    public EditProfileViewModel(Context context, ActivityEditProfileBinding editProfileBinding) {
        this.context = context;
        this.editProfileBinding = editProfileBinding;
        user = User.getInstance();
        setEmail(User.getInstance().getEmail());
        setCity(User.getInstance().getCityName());
        setProfileImageUrl(User.getInstance().getProfileImageUrl());
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        notifyPropertyChanged(BR.profileImageUrl);
    }

    //    method to save the user settings
    public void updateProfile() {
        ((Activity) context).finish();
    }

    //  method to select/change city from the list of cities
    public void selectCity() {
        ((Activity) context).startActivityForResult(new Intent(context, CityActivity.class), REQUEST_CITY);
    }

    //    change user profile image
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeProfileImage() {
        ((EditProfileActivity) context).selectImage();

    }


    public void uploadImageOnCloudinary(String imagePath) {

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_API_KEY, CloudinaryDetail.API_KEY);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_API_SECRET, CloudinaryDetail.API_SECRET);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_CLOUD_NAME, CloudinaryDetail.CLOUD_NAME);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_IMAGE_PATH, imagePath);

        UploadImageAsync uploadImageAsync = new UploadImageAsync(context, paramMap, new UploadImageAsync.OnCompleteListener() {
            @Override
            public void onComplete(Map<String, String> uploadResult) {

                Logger.d("uploadResult", uploadResult.toString());

            }
        });
        uploadImageAsync.execute();

    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }
}
