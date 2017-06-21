package com.kokaihop.userprofile;

import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class OtherUserProfileViewModel extends BaseViewModel {

    private UserDataListener userDataListener;
    private Context context;
    private String accessToken, friendlyUrl;
    private String countryCode = Constants.COUNTRY_CODE;
    private ProfileDataManager profileDataManager;


    public OtherUserProfileViewModel(Context context, UserDataListener userDataListener) {
        this.userDataListener = userDataListener;
        this.context = context;
        profileDataManager = new ProfileDataManager();
    }

    @Override
    protected void destroy() {

    }

    public void getUserData(final String userId) {
        setProgressVisible(true);
        friendlyUrl = getFriendlyUrlFromDB(userId);
        new ProfileApiHelper().getOtherUserData(accessToken, friendlyUrl, countryCode, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    jsonObject = jsonObject.getJSONObject("user");
                    new ProfileDataManager().insertOrUpdateUserDataUsingJSON(jsonObject);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                fetchUserDataFromDB(userId);
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);

            }
        });
    }

    public void fetchUserDataFromDB(String userId) {
        profileDataManager.fetchUserData(userId, User.getOtherUser());
        userDataListener.showUserProfile();
    }

    public String getFriendlyUrlFromDB(String userId) {
        return profileDataManager.getFriendlyUrlOfUser(userId);
    }

    public void onToggleFollowing(User user) {

        String userId = user.get_id();
        user.setFollowByMe(!user.isFollowByMe());
        if (user.isFollowByMe()) {
            User.getInstance().getFollowing().add(user.get_id());
        } else {
            User.getInstance().getFollowing().remove(user.get_id());
        }

        toggleFollowing(userId, user);
        userDataListener.followToggeled();

    }

    private void toggleFollowing(String friendId, final User user) {
        final boolean followByMe = user.isFollowByMe();
        setUpApiCall();
        ToggleFollowingRequest request = new ToggleFollowingRequest();
        request.setFriendId(friendId);
        request.setFollowRequest(followByMe);
        new ProfileApiHelper().toggleFollowing(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                if (followByMe) {
                    Toast.makeText(context, R.string.follow_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.unfollow_success, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                user.setFollowByMe(!followByMe);
            }

            @Override
            public void onError(Object response) {
                user.setFollowByMe(!followByMe);
            }
        });
    }

    private void setUpApiCall() {
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        accessToken = Constants.AUTHORIZATION_BEARER + token;
    }
}
