package com.kokaihop.userprofile;

import com.kokaihop.database.UserRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.userprofile.model.FollowingToggleResponse;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;

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

    public void getUserData(String accessToken, String languageCode, final IApiRequestComplete successInterface){
        Call<UserRealmObject> userResponseCall = userProfileApi.getUserData(accessToken,languageCode);
        userResponseCall.enqueue(new ResponseHandler<UserRealmObject>(successInterface));
    }

}
