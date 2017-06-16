package com.kokaihop.userprofile;

import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentUserProfileBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.editprofile.SettingsApiHelper;
import com.kokaihop.editprofile.SettingsResponse;
import com.kokaihop.editprofile.model.ProfileImageUpdateRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.User;
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
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserProfileViewModel extends BaseViewModel {

    private UserDataListener userDataListener;
    private Context context;
    private String userId, accessToken;
    private String countryCode = Constants.COUNTRY_CODE;
    private ProfileDataManager profileDataManager;
    FragmentUserProfileBinding binding;


    public UserProfileViewModel(Context context, UserDataListener userDataListener, FragmentUserProfileBinding binding) {
        this.userDataListener = userDataListener;
        this.context = context;
        this.binding = binding;
        profileDataManager = new ProfileDataManager();
    }

    @Override
    protected void destroy() {

    }

    public void getUserData() {
        setProgressVisible(true);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String accessToken = bearer + token;
        Logger.e(bearer, token);
        new ProfileApiHelper().getUserData(accessToken, countryCode, new IApiRequestComplete<UserRealmObject>() {
            @Override
            public void onSuccess(UserRealmObject response) {
                userId = response.getId();
                profileDataManager.insertOrUpdateUserData(response);
                fetchUserDataFromDB();
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

    public void fetchUserDataFromDB() {
        if (userId == null) {
            userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        }
        profileDataManager.fetchUserData(userId);
        userDataListener.showUserProfile();
    }


    public void uploadImageOnCloudinary(String imagePath) {

        HashMap<String, String> paramMap = CloudinaryUtils.getCloudinaryParams(imagePath);
        setProgressVisible(true);
        UploadImageAsync uploadImageAsync = new UploadImageAsync(context, paramMap, new UploadImageAsync.OnCompleteListener() {
            @Override
            public void onComplete(Map<String, String> uploadResult) throws ParseException {

                if(uploadResult!=null){
                    User user = User.getInstance();
                    user.setProfileImage(new CloudinaryImage());
                    user.getProfileImage().setCloudinaryId(uploadResult.get("public_id"));
                    user.getProfileImage().setUploaded(new Date().getTime());
                    updateProfilePic();
                }else {
                    Toast.makeText(context, R.string.something_went_wrong,Toast.LENGTH_SHORT);
                }
                setProgressVisible(false);
            }
        });
        uploadImageAsync.execute();

    }

    public void setupApiCall() {
        accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
    }

    public void updateProfilePic() {
        setProgressVisible(true);
        User user = User.getInstance();
        setupApiCall();
        ProfileImageUpdateRequest request = new ProfileImageUpdateRequest();
        request.setProfileImage(user.getProfileImage());
        new SettingsApiHelper().changeProfilePicture(accessToken, userId, request, new IApiRequestComplete<SettingsResponse>() {
            @Override
            public void onSuccess(SettingsResponse response) {
                new ProfileApiHelper().getUserData(accessToken, Constants.COUNTRY_CODE, new IApiRequestComplete<UserRealmObject>() {
                    @Override
                    public void onSuccess(UserRealmObject response) {
                        Toast.makeText(context, R.string.profile_pic_uploaded, Toast.LENGTH_SHORT).show();
                        ProfileDataManager profileDataManager = new ProfileDataManager();
                        profileDataManager.insertOrUpdateUserData(response);
                        profileDataManager.fetchUserData(userId);
//                        setProfileImage();
                        getUserData();
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
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SettingsResponse response) {
                setProgressVisible(false);
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void setProfileImage() {
//        int width = context.getResources().getDimensionPixelSize(R.dimen.user_profile_pic_size);
//        int height = width;
//        ImageView ivProfile = binding.userAvatar;
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivProfile.getLayoutParams();
//        layoutParams.height = height;
//        layoutParams.width = width;
//        ivProfile.setLayoutParams(layoutParams);
//        LinearLayout.LayoutParams ivProfileLayoutParams = (LinearLayout.LayoutParams) ivProfile.getLayoutParams();
//        CloudinaryImage profileImage = User.getInstance().getProfileImage();
//        if (profileImage != null) {
//            String imageUrl = CloudinaryUtils.getRoundedImageUrl(profileImage.getCloudinaryId(), String.valueOf(ivProfileLayoutParams.width), String.valueOf(ivProfileLayoutParams.height));
//            User.getInstance().setProfileImageUrl(imageUrl);
//            setProfileImageUrl(imageUrl);
//        }
//        editProfileBinding.executePendingBindings();
//    }

}
