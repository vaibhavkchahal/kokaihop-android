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
import com.kokaihop.home.userprofile.model.User;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

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
    private boolean isDownloading = true;
    private int max = 20;
    private int offset = 0;
    private int totalFollowing;
    private int totalFollowers;

    public FollowersFollowingViewModel(Context context) {
        this.context = context;
    }

    public FollowersFollowingViewModel(UserApiCallback userApiCallback, Context context) {
        this.max = 20;
        this.offset = 0;
        this.userApiCallback = userApiCallback;
        this.context = context;
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(int totalFollowing) {
        this.totalFollowing = totalFollowing;
    }
//Getting list of following users through api call

    public void getFollowingUsers(final int offset) {
        setOffset(offset);
        setDownloading(isDownloading);

        setProgressVisible(true);
        setUpApiCall();
        setProgressVisible(true);
        if (isDownloading) {
            Logger.e("Get", "Downloading");

            Call<FollowingFollowersApiResponse> followingApiResponseCall = userProfileApi.getFollowingUsers(accessToken, userId, max, getOffset());

            followingApiResponseCall.enqueue(new Callback<FollowingFollowersApiResponse>() {
                @Override
                public void onResponse(Call<FollowingFollowersApiResponse> call, Response<FollowingFollowersApiResponse> response) {
                    ArrayList<FollowingFollowerUser> userList = response.body().getUsers();
                    FollowingFollowersApiResponse.getFollowingApiResponse().getUsers().addAll(userList);
                    FollowingFollowersApiResponse.getFollowingApiResponse().setTotal(response.body().getTotal());
                    setTotalFollowing(response.body().getTotal());
                    Logger.e("List Size", FollowingFollowersApiResponse.getFollowingApiResponse().getUsers().size() + "");

                    if (getOffset() + getMax() >= getTotalFollowing()) {
                        setDownloading(false);
                    }
                    setProgressVisible(false);
                    userApiCallback.showUserProfile();
                }

                @Override
                public void onFailure(Call<FollowingFollowersApiResponse> call, Throwable t) {
                    Logger.e("Error", t.toString());
                    setProgressVisible(false);

                }
            });
        }

    }

    //Getting list of followers through api call
    public void getFollowers(final int offset) {
        setOffset(offset);
        setDownloading(isDownloading);
        setProgressVisible(true);
        setUpApiCall();

        if (isDownloading) {
            Call<FollowingFollowersApiResponse> followersApiResponseCall = userProfileApi.getFollowers(accessToken, userId, getMax(), getOffset());

            followersApiResponseCall.enqueue(new Callback<FollowingFollowersApiResponse>() {
                @Override
                public void onResponse(Call<FollowingFollowersApiResponse> call, Response<FollowingFollowersApiResponse> response) {
                    ArrayList<FollowingFollowerUser> userList = response.body().getUsers();
                    FollowingFollowersApiResponse.getFollowersApiResponse().getUsers().addAll(userList);
                    FollowingFollowersApiResponse.getFollowersApiResponse().setTotal(response.body().getTotal());
                    setTotalFollowers(response.body().getTotal());
                    Logger.e("Followers Size", FollowingFollowersApiResponse.getFollowersApiResponse().getUsers().size() + "");
                    if (getOffset() + getMax() >= getTotalFollowers()) {
                        setDownloading(false);
                    }
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

    //Seting the access token for the api calls

    public void setUpApiCall() {
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
//        accessToken = bearer + token;
//        userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);

        userId = "56387ade1a258f0300c3074e";
        accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
    }

    @Override
    protected void destroy() {

    }

    // method to manage the data before follow or unfollow the user

    public void onToggleFollowing(View checkbox, FollowingFollowerUser user) {

        String userId = user.get_id();

        if (((CheckBox) checkbox).isChecked()) {
            User.getInstance().getFollowing().add(user.get_id());
        } else {
            User.getInstance().getFollowing().remove(user.get_id());
        }
        toggleFollowing(userId, ((CheckBox) checkbox).isChecked());
        userApiCallback.followToggeled();

    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }
}
