package com.kokaihop.home.userprofile;

import android.content.Context;
import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.userprofile.model.FollowingApiResponse;
import com.kokaihop.home.userprofile.model.FollowingToggleResponse;
import com.kokaihop.home.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowingViewModel extends BaseViewModel{

    private UserApiCallback userApiCallback;
    private Context context;
    private UserProfileApi userProfileApi;
    private String accessToken;

    public FollowingViewModel(Context context){
        this.context = context;
    }
    public FollowingViewModel(UserApiCallback userApiCallback, Context context) {
        this.userApiCallback = userApiCallback;
        this.context = context;
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
    }

    public void getFollowingUsers(){
        setUpApiCall();
        setProgressVisible(true);
        Log.e("Get","Following");
//        String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        String userId = "56387ade1a258f0300c3074e";
        Call<FollowingApiResponse> followingApiResponseCall = userProfileApi.getUserFollowing(accessToken,userId,20,0);

        followingApiResponseCall.enqueue(new Callback<FollowingApiResponse>() {
            @Override
            public void onResponse(Call<FollowingApiResponse> call, Response<FollowingApiResponse> response) {
                Log.e("Name",response.body().getTotal()+"");
                FollowingApiResponse.setFollowingApiResponse(response.body());
                setProgressVisible(false);
                userApiCallback.showUserProfile();
            }

            @Override
            public void onFailure(Call<FollowingApiResponse> call, Throwable t) {
                Log.e("Error",t.toString());
                setProgressVisible(false);

            }
        });
    }

    public  void toggleFollowing(String friendId, boolean followRequest){
        setUpApiCall();
        ToggleFollowingRequest request = new ToggleFollowingRequest();
        request.setFriendId(friendId);
        request.setFollowRequest(followRequest);
        Call<FollowingToggleResponse> followingToggleResponseCall = userProfileApi.toggleFollowing(accessToken,request);

        followingToggleResponseCall.enqueue(new Callback<FollowingToggleResponse>() {
            @Override
            public void onResponse(Call<FollowingToggleResponse> call, Response<FollowingToggleResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowingToggleResponse> call, Throwable t) {

            }
        });
    }

    public void setUpApiCall(){
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        String bearer = Constants.AUTHORIZATION_BEARER;
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
    }
    @Override
    protected void destroy() {

    }
}
