package com.kokaihop.home.userprofile;

import com.kokaihop.home.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.home.userprofile.model.FollowingToggleResponse;
import com.kokaihop.home.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.home.userprofile.model.User;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class ProfileApiHelper {
    private UserProfileApi userProfileApi;

    public ProfileApiHelper() {
        this.userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
    }


    public void getFollowers(String accessToken, String userId, int max, int offset, final IApiRequestComplete successInterface){
        Call<FollowingFollowersApiResponse> followersResponseCall = userProfileApi.getFollowers(accessToken,userId,max,offset);
        followersResponseCall.enqueue(new ResponseHandler<FollowingFollowersApiResponse>(successInterface));
    }

    public void getFollowing(String accessToken, String userId, int max, int offset, final IApiRequestComplete successInterface){
        Call<FollowingFollowersApiResponse> followingUsersResponseCall = userProfileApi.getFollowingUsers(accessToken,userId,max,offset);
        followingUsersResponseCall.enqueue(new ResponseHandler<FollowingFollowersApiResponse>(successInterface));
    }

    public void toggleFollowing(String accessToken, ToggleFollowingRequest toggleFollowingRequest, final IApiRequestComplete successInterface){
        Call<FollowingToggleResponse> followingToggleResponseCall = userProfileApi.toggleFollowing(accessToken,toggleFollowingRequest);
        followingToggleResponseCall.enqueue(new ResponseHandler<FollowingToggleResponse>(successInterface));
    }

    public void getUserData(String accessToken, String laanguageCode, final IApiRequestComplete successInterface){
        Call<User> userResponseCall = userProfileApi.getUserData(accessToken,laanguageCode);
        userResponseCall.enqueue(new ResponseHandler<User>(successInterface));
    }

}
