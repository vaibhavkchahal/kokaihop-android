package com.kokaihop.userprofile;

import android.content.Context;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserProfileViewModel extends BaseViewModel {

    private UserDataListener userDataListener;
    private Context context;
    private String userId;
    private String countryCode = "en";
    private ProfileDataManager profileDataManager;


    public UserProfileViewModel(Context context, UserDataListener userDataListener) {
        this.userDataListener = userDataListener;
        this.context = context;
        profileDataManager = new ProfileDataManager();
    }

    @Override
    protected void destroy() {

    }

    public void getUserData() {
        setProgressVisible(true);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
//        String accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        String accessToken = bearer + token;
        Logger.e(bearer, token);
        new ProfileApiHelper().getUserData(accessToken, countryCode, new IApiRequestComplete<UserRealmObject>() {
            @Override
            public void onSuccess(UserRealmObject response) {
                Logger.e("User ID : ", response.getId());
                userId = response.getId();
                Logger.e("User ID : ", userId);
//                User.setUser(response);
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

    public void changeProfilePic() {
        Logger.e("Profile Pic", "Changed");
    }
}
