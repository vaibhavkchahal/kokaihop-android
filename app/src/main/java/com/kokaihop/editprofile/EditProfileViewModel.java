package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEditProfileBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.city.CityActivity;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.editprofile.model.CityLiving;
import com.kokaihop.editprofile.model.CityLocation;
import com.kokaihop.editprofile.model.CityUpdateRequest;
import com.kokaihop.editprofile.model.ProfileImageUpdateRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.ProfileApiHelper;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CloudinaryDetail;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.UploadImageAsync;

import java.text.ParseException;
import java.util.Date;
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
    private String email, profileImageUrl, cityName, accessToken, userId;
    private CityLocation city;
    private SettingsApiHelper settingsApiHelper;
    private User user;

    public EditProfileViewModel(Context context, ActivityEditProfileBinding editProfileBinding) {
        this.context = context;
        this.editProfileBinding = editProfileBinding;
        user = User.getInstance();
        setEmail(User.getInstance().getEmail());
        setCityName(user.getCityName());
        setProfileImageUrl(User.getInstance().getProfileImageUrl());
        city = new CityLocation();
        city.setLiving(new CityLiving());
        settingsApiHelper = new SettingsApiHelper();
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
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
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
        setProgressVisible(true);
        UploadImageAsync uploadImageAsync = new UploadImageAsync(context, paramMap, new UploadImageAsync.OnCompleteListener() {
            @Override
            public void onComplete(Map<String, String> uploadResult) throws ParseException {

                Logger.d("uploadResult", uploadResult.toString());
                User user = User.getInstance();
                user.setProfileImage(new CloudinaryImage());
                user.getProfileImage().setCloudinaryId(uploadResult.get("public_id"));
                user.getProfileImage().setUploaded(new Date().getTime());
                updateProfilePic();
                setProgressVisible(false);
            }
        });
        uploadImageAsync.execute();

    }

    public void updateProfilePic() {
        setProgressVisible(true);
        setupApiCall();
        ProfileImageUpdateRequest request = new ProfileImageUpdateRequest();
        request.setProfileImage(user.getProfileImage());
        settingsApiHelper.changeProfilePicture(accessToken, userId, request, new IApiRequestComplete<SettingsResponse>() {
            @Override
            public void onSuccess(SettingsResponse response) {
                new ProfileApiHelper().getUserData(accessToken, Constants.LANGUGE_CODE, new IApiRequestComplete<UserRealmObject>() {
                    @Override
                    public void onSuccess(UserRealmObject response) {
                        Toast.makeText(context, "Profile Picture uploaded Successfully", Toast.LENGTH_SHORT).show();
                        ProfileDataManager profileDataManager = new ProfileDataManager();
                        profileDataManager.insertOrUpdateUserData(response);
                        profileDataManager.fetchUserData(userId);
                        setProfileImage();
                        setProgressVisible(false);
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                    }

                    @Override
                    public void onError(UserRealmObject response) {
                        setProgressVisible(false);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, "Error while updating profile picture", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SettingsResponse response) {
                setProgressVisible(false);
                Toast.makeText(context, "Error while updating profile picture", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCity() {
        setProgressVisible(true);
        setupApiCall();
        CityUpdateRequest request = new CityUpdateRequest();
        request.setLocation(city);
        settingsApiHelper.changeCity(accessToken, userId, request, new IApiRequestComplete<SettingsResponse>() {
            @Override
            public void onSuccess(SettingsResponse response) {
                setProgressVisible(false);
                user.setCityName(city.getLiving().getName());
                Toast.makeText(context, "City updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SettingsResponse response) {
                setProgressVisible(false);
                Toast.makeText(context, "Error while updating city", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setupApiCall() {
        accessToken = Constants.AUTHORIZATION_BEARER  + SharedPrefUtils.getSharedPrefStringData(context,Constants.ACCESS_TOKEN);
        userId = SharedPrefUtils.getSharedPrefStringData(context,Constants.USER_ID);
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }

    public CityLocation getCity() {
        return city;
    }

    public void setCity(CityLocation city) {
        this.city = city;
    }

    public void setProfileImage() {
        int width = context.getResources().getDimensionPixelSize(R.dimen.user_profile_pic_size);
        int height = width;
        ImageView ivProfile = editProfileBinding.ivUserProfilePic;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivProfile.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        ivProfile.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams ivProfileLayoutParams = (LinearLayout.LayoutParams) ivProfile.getLayoutParams();
        CloudinaryImage profileImage = User.getInstance().getProfileImage();
        if (profileImage != null) {
            String imageUrl = CloudinaryUtils.getRoundedImageUrl(profileImage.getCloudinaryId(), String.valueOf(ivProfileLayoutParams.width), String.valueOf(ivProfileLayoutParams.height));
            User.getInstance().setProfileImageUrl(imageUrl);
            setProfileImageUrl(imageUrl);
        }
        editProfileBinding.executePendingBindings();
    }
}
