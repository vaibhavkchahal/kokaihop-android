package com.kokaihop.userprofile;

import com.kokaihop.database.UserRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.userprofile.model.FollowingToggleResponse;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;

;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class ProfileApiHelper {
    private UserProfileApi userProfileApi;

    public ProfileApiHelper() {
        this.userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
    }

    //    get the followers of the user
    public void getFollowers(String accessToken, String userId, int max, int offset, final IApiRequestComplete successInterface) {
        Call<FollowingFollowersApiResponse> followersResponseCall = userProfileApi.getFollowers(accessToken, userId, max, offset);
        followersResponseCall.enqueue(new ResponseHandler<FollowingFollowersApiResponse>(successInterface));
    }

    //    get the following of the user
    public void getFollowing(String accessToken, String userId, int max, int offset, final IApiRequestComplete successInterface) {
        Call<FollowingFollowersApiResponse> followingUsersResponseCall = userProfileApi.getFollowingUsers(accessToken, userId, max, offset);
        followingUsersResponseCall.enqueue(new ResponseHandler<FollowingFollowersApiResponse>(successInterface));
    }

    //    follow or unfollow the user
    public void toggleFollowing(String accessToken, ToggleFollowingRequest toggleFollowingRequest, final IApiRequestComplete successInterface) {
        Call<FollowingToggleResponse> followingToggleResponseCall = userProfileApi.toggleFollowing(accessToken, toggleFollowingRequest);
        followingToggleResponseCall.enqueue(new ResponseHandler<FollowingToggleResponse>(successInterface));
    }

    //    get the user data from the api
    public void getUserData(String accessToken, String languageCode, final IApiRequestComplete successInterface) {
        Call<UserRealmObject> userResponseCall = userProfileApi.getUserData(accessToken, languageCode);
        userResponseCall.enqueue(new ResponseHandler<UserRealmObject>(successInterface));
    }

//    get the recipes of the user from the api.
    public void getRecipesOfUser(String userId, int offset, int max, final IApiRequestComplete successInterface) {
        Call<ResponseBody> userResponseCall = userProfileApi.getRecipesOfUser(userId, offset, max);
        userResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }

}
