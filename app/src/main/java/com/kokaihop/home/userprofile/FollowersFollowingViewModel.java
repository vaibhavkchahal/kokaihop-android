package com.kokaihop.home.userprofile;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.userprofile.model.FollowingFollowerUser;
import com.kokaihop.home.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.home.userprofile.model.FollowingToggleResponse;
import com.kokaihop.home.userprofile.model.ToggleFollowingRequest;
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

public class FollowersFollowingViewModel extends BaseViewModel {

    private UserApiCallback userApiCallback;
    private Context context;
    private UserProfileApi userProfileApi;
    private String accessToken;
    private String userId;

    public FollowersFollowingViewModel(Context context) {
        this.context = context;
    }

    public FollowersFollowingViewModel(UserApiCallback userApiCallback, Context context) {
        this.userApiCallback = userApiCallback;
        this.context = context;
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
    }


    //Getting list of following users through api call

    public void getFollowingUsers() {
        setProgressVisible(true);
        setUpApiCall();
        setProgressVisible(true);
        Log.e("Get", "Following");
        Call<FollowingFollowersApiResponse> followingApiResponseCall = userProfileApi.getFollowingUsers(accessToken, userId, 20, 0);

        followingApiResponseCall.enqueue(new Callback<FollowingFollowersApiResponse>() {
            @Override
            public void onResponse(Call<FollowingFollowersApiResponse> call, Response<FollowingFollowersApiResponse> response) {
                Log.e("Name", response.body().getTotal() + "");
                FollowingFollowersApiResponse.setFollowingApiResponse(response.body());
                setProgressVisible(false);
                userApiCallback.showUserProfile();
            }

            @Override
            public void onFailure(Call<FollowingFollowersApiResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                setProgressVisible(false);

            }
        });
    }

    //API call to follow or unfollow a user
    public void toggleFollowing(String friendId, boolean followRequest) {
        setUpApiCall();
        ToggleFollowingRequest request = new ToggleFollowingRequest();
        request.setFriendId(friendId);
        request.setFollowRequest(followRequest);
        Call<FollowingToggleResponse> followingToggleResponseCall = userProfileApi.toggleFollowing(accessToken, request);

        followingToggleResponseCall.enqueue(new Callback<FollowingToggleResponse>() {
            @Override
            public void onResponse(Call<FollowingToggleResponse> call, Response<FollowingToggleResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowingToggleResponse> call, Throwable t) {

            }
        });
    }

    //Getting list of followers through api call
    public void getFollowers() {
        setProgressVisible(true);
        setUpApiCall();
        Log.e("Get", "Following");
        Call<FollowingFollowersApiResponse> followersApiResponseCall = userProfileApi.getFollowers(accessToken, userId, 20, 0);

        followersApiResponseCall.enqueue(new Callback<FollowingFollowersApiResponse>() {
            @Override
            public void onResponse(Call<FollowingFollowersApiResponse> call, Response<FollowingFollowersApiResponse> response) {
                Log.e("Name", response.body().getTotal() + "");
                FollowingFollowersApiResponse.setFollowersApiResponse(response.body());
                setProgressVisible(false);
                userApiCallback.showUserProfile();
            }

            @Override
            public void onFailure(Call<FollowingFollowersApiResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                setProgressVisible(false);

            }
        });
    }

    //Seting the access token for the api calls

    public void setUpApiCall() {
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        accessToken = bearer + token;
        userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);

//        userId = "56387ade1a258f0300c3074e";
//        accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
    }

    @Override
    protected void destroy() {

    }

    // method to manage the data before follow or unfollow the user

    public void onToggleFollowing(View checkbox, FollowingFollowerUser user) {

        String userId = user.get_id();

        if (((CheckBox) checkbox).isChecked()) {
            UserApiResponse.getInstance().getFollowing().add(user.get_id());
        } else {
            UserApiResponse.getInstance().getFollowing().remove(user.get_id());
        }
        toggleFollowing(userId, ((CheckBox) checkbox).isChecked());
        userApiCallback.followToggeled();

    }
}
