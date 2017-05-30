package com.kokaihop.userprofile;

import android.content.Context;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserProfileViewModel extends BaseViewModel {

    private UserApiCallback userApiCallback;
    private Context context;
    private String countryCode = "en";


    public UserProfileViewModel(Context context, UserApiCallback userApiCallback) {
        this.userApiCallback = userApiCallback;
        this.context = context;
    }

    @Override
    protected void destroy() {

    }

    public void getUserData() {
        setProgressVisible(true);

        UserProfileApi userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
//        String accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        String accessToken = bearer + token;
        new ProfileApiHelper().getUserData(accessToken, countryCode, new IApiRequestComplete<User>() {
            @Override
            public void onSuccess(User response) {
                Logger.e("User ID : ", response.get_id());
                User.setUser(response);
                userApiCallback.showUserProfile();
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);

            }

            @Override
            public void onError(User response) {
                setProgressVisible(false);

            }
        });
    }
}
