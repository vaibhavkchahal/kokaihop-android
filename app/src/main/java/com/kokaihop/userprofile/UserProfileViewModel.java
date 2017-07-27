package com.kokaihop.userprofile;

import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentUserProfileBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.editprofile.SettingsApiHelper;
import com.kokaihop.editprofile.SettingsResponse;
import com.kokaihop.editprofile.model.ProfileImageUpdateRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.UploadImageAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

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
        userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        String accessToken = bearer + token;
        Logger.e(bearer, token);
        new ProfileApiHelper().getUserData(accessToken, countryCode, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
//                    jsonObject = jsonObject.getJSONObject("user");
                    profileDataManager.insertOrUpdateUserDataUsingJSON(jsonObject);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                fetchUserDataFromDB();
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);

            }
        });
    }

    public void fetchUserDataFromDB() {
        if (userId == null) {
            userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        }
        profileDataManager.fetchUserData(userId, User.getInstance());
        userDataListener.showUserProfile();
    }


    public void uploadImageOnCloudinary(String imagePath) {
        GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.photo_upload_category), context.getString(R.string.photo_upload_action), context.getString(R.string.profile_photo_upload_label));


        HashMap<String, String> paramMap = CloudinaryUtils.getCloudinaryParams(imagePath);
        setProgressVisible(true);
        UploadImageAsync uploadImageAsync = new UploadImageAsync(context, paramMap, new UploadImageAsync.OnCompleteListener() {
            @Override
            public void onComplete(Map<String, String> uploadResult) throws ParseException {

                if (uploadResult != null) {
                    User user = User.getInstance();
                    user.setProfileImage(new CloudinaryImage());
                    user.getProfileImage().setCloudinaryId(uploadResult.get("public_id"));
                    user.getProfileImage().setUploaded(new Date().getTime());
                    updateProfilePic();
                } else {
                    Toast.makeText(context, context.getString(R.string.profile_pic_upload_failed), Toast.LENGTH_SHORT).show();
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
        final User user = User.getInstance();
        setupApiCall();
        ProfileImageUpdateRequest request = new ProfileImageUpdateRequest();
        request.setProfileImage(user.getProfileImage());
        new SettingsApiHelper().changeProfilePicture(accessToken, userId, request, new IApiRequestComplete<SettingsResponse>() {
            @Override
            public void onSuccess(SettingsResponse response) {
                new ProfileApiHelper().getUserData(accessToken, Constants.COUNTRY_CODE, new IApiRequestComplete() {
                    @Override
                    public void onSuccess(Object response) {

                        GoogleAnalyticsHelper.trackEventAction( context.getString(R.string.photo_upload_category), context.getString(R.string.uploaded_photo_action), context.getString(R.string.profile_photo_uploaded_label));

                        AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.profile_pic_upload_success));
                        ProfileDataManager profileDataManager = new ProfileDataManager();
                        ResponseBody responseBody = (ResponseBody) response;
                        JSONObject userJson;
                        try {
                            userJson = new JSONObject(responseBody.string());
                            profileDataManager.insertOrUpdateUserDataUsingJSON(userJson);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        fetchUserDataFromDB();
                        setProgressVisible(false);
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                    }

                    @Override
                    public void onError(Object response) {
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
}
