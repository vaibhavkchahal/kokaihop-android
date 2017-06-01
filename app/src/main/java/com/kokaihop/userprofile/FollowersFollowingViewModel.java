package com.kokaihop.userprofile;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.Toast;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.RetrofitClient;
import com.kokaihop.userprofile.model.FollowersFollowingList;
import com.kokaihop.userprofile.model.FollowingFollowerUser;
import com.kokaihop.userprofile.model.FollowingFollowersApiResponse;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class FollowersFollowingViewModel extends BaseViewModel {

    private UserDataListener userDataListener;
    private Context context;
    private UserProfileApi userProfileApi;
    private String accessToken;
    private String userId;
    private boolean isDownloading = true;
    private int max = 20;
    private int offset = 0;
    private int totalFollowing;
    private int totalFollowers;
    ProfileDataManager profileDataManager;

    public FollowersFollowingViewModel(Context context) {
        this.context = context;
    }

    public FollowersFollowingViewModel(UserDataListener userDataListener, Context context) {
        this.max = 20;
        this.offset = 0;
        this.userDataListener = userDataListener;
        this.context = context;
        userProfileApi = RetrofitClient.getInstance().create(UserProfileApi.class);
        profileDataManager = new ProfileDataManager();

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
            new ProfileApiHelper().getFollowing(accessToken, userId, getMax(), getOffset(), new IApiRequestComplete<FollowingFollowersApiResponse>() {
                @Override
                public void onSuccess(FollowingFollowersApiResponse response) {
                    RealmList<UserRealmObject> userList = response.getUsers();
                    profileDataManager.insertOrUpdateFollowing(userList,userId);

                    FollowersFollowingList.getFollowingList().setTotal(response.getTotal());
                    setTotalFollowing(response.getTotal());
                    if (getOffset() + getMax() >= getTotalFollowing()) {
                        setDownloading(false);
                    }
                    setProgressVisible(false);
                    fetchFollowersFromDB();
                }

                @Override
                public void onFailure(String message) {
                    Logger.e("Error", message);
                    setDownloading(false);
                    setProgressVisible(false);
                    userDataListener.showUserProfile();
                }

                @Override
                public void onError(FollowingFollowersApiResponse response) {
                    setDownloading(false);
                    setProgressVisible(false);
                    userDataListener.showUserProfile();
                }
            });
            Logger.e("Get", "Downloading");
        }

    }

    public void fetchFollowersFromDB(){
        ArrayList<FollowingFollowerUser> followingList;

        followingList = profileDataManager.fetchFollowingList(userId);
        FollowersFollowingList.getFollowingList().getUsers().addAll(followingList);
        userDataListener.showUserProfile();
    }

    //Getting list of followers through api call
    public void getFollowers(final int offset) {
        setOffset(offset);
        setDownloading(isDownloading);
        setProgressVisible(true);
        setUpApiCall();

        if (isDownloading) {
            new ProfileApiHelper().getFollowers(accessToken, userId, getMax(), getOffset(), new IApiRequestComplete<FollowingFollowersApiResponse>() {
                @Override
                public void onSuccess(FollowingFollowersApiResponse response) {
                    RealmList<UserRealmObject> userList = response.getUsers();
                    profileDataManager.insertOrUpdateFollowers(userList, userId);

                    ArrayList<FollowingFollowerUser> followersList = new ArrayList<FollowingFollowerUser>();
                    followersList =  profileDataManager.fetchFollowersList(userId);
                    FollowersFollowingList.getFollowersList().getUsers().addAll(followersList);
                    FollowersFollowingList.getFollowingList().setTotal(response.getTotal());

                    setTotalFollowers(response.getTotal());

                    if (getOffset() + getMax() >= getTotalFollowers()) {
                        setDownloading(false);
                    }
                    setProgressVisible(false);
                    userDataListener.showUserProfile();
                }

                @Override
                public void onFailure(String message) {
                    setDownloading(false);
                    setProgressVisible(false);

                }

                @Override
                public void onError(FollowingFollowersApiResponse response) {
                    setDownloading(false);
                    setProgressVisible(false);

                }
            });
        }
    }

    //API call to follow or unfollow a user
    public void toggleFollowing(String friendId, final CheckBox checkBox) {
        setUpApiCall();
        ToggleFollowingRequest request = new ToggleFollowingRequest();
        request.setFriendId(friendId);
        request.setFollowRequest(checkBox.isChecked());
        new ProfileApiHelper().toggleFollowing(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                if(checkBox.isChecked()){
                    Toast.makeText(context,"Follow Successful",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Unfollow Successful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                checkBox.setChecked(!checkBox.isChecked());

            }

            @Override
            public void onError(Object response) {
                checkBox.setChecked(!checkBox.isChecked());
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

        Logger.e("token : ", token);

//        userId = "56387ade1a258f0300c3074e";
//        accessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
    }

    @Override
    protected void destroy() {

    }

    // method to manage the data before follow or unfollow the user

    public void onToggleFollowing(CheckBox checkbox, FollowingFollowerUser user) {

        String userId = user.get_id();

        if (((CheckBox) checkbox).isChecked()) {
            User.getInstance().getFollowing().add(user.get_id());
        } else {
            User.getInstance().getFollowing().remove(user.get_id());
        }
        toggleFollowing(userId, checkbox);
        userDataListener.followToggeled();

    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }
}
