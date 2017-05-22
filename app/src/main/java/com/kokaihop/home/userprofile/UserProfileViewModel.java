package com.kokaihop.home.userprofile;

import android.content.Context;
import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.userprofile.model.FollowingApiResponse;
import com.kokaihop.home.userprofile.model.UserApiResponse;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class UserProfileViewModel extends BaseViewModel{

    private UserApiResponse userApiResponse;
    private UserApiCallback userApiCallback;
    private Context context;
    private String countryCode =  "en";


    public UserProfileViewModel(Context context, UserApiCallback userApiCallback) {
        this.userApiCallback  = userApiCallback;
        this.context = context;
    }

    @Override
    protected void destroy() {

    }

    public void getUserData() {
        setProgressVisible(true);
        UserProfileApi userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String arguments = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        Call<UserApiResponse> userApiCall =  userProfileApi.getUserData(arguments,countryCode);
        userApiCall.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                Log.e("Full Name : ", response.body().get_id());
                setUserApiResponse(response.body());
                userApiCallback.showUserProfile(userApiResponse);
                setProgressVisible(false);
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                setProgressVisible(false);
            }
        });
    }

    public void getFollowingUsers(){
        setProgressVisible(true);
        Log.e("Get","Following");
        UserProfileApi userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String arguments = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        Call<FollowingApiResponse> followingApiResponseCall = userProfileApi.getUserFollowing(arguments,userApiResponse.get_id(),20,0);
        followingApiResponseCall.enqueue(new Callback<FollowingApiResponse>() {
            @Override
            public void onResponse(Call<FollowingApiResponse> call, Response<FollowingApiResponse> response) {
                Log.e("Name",response.body().getTotal()+"");
                setProgressVisible(false);
            }

            @Override
            public void onFailure(Call<FollowingApiResponse> call, Throwable t) {
                Log.e("Error",t.toString());
                setProgressVisible(false);
            }
        });
    }

    public UserApiResponse getUserApiResponse() {
        return userApiResponse;
    }

    public void setUserApiResponse(UserApiResponse userApiResponse) {
        this.userApiResponse = userApiResponse;
    }
}
