package com.kokaihop.home.userprofile;

import android.content.Context;
import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.userprofile.model.UserApiResponse;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Log.e(bearer,token);
        String accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        Log.e("Token",accessToken);
        Call<UserApiResponse> userApiCall = userProfileApi.getUserData(accessToken, countryCode);
        userApiCall.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                Log.e("User ID : ", response.body().get_id());
                UserApiResponse.setUserApiResponse(response.body());
                userApiCallback.showUserProfile();
                setProgressVisible(false);
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                setProgressVisible(false);
            }
        });
    }
}
