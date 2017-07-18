package com.kokaihop.userprofile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.ToggleFollowingRequest;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
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
    private String accessToken;
    private String countryCode = Constants.COUNTRY_CODE;
    private ProfileDataManager profileDataManager;
    private User user;


    public OtherUserProfileViewModel(Context context, UserDataListener userDataListener, User user) {
        this.userDataListener = userDataListener;
        this.context = context;
        this.user = user;
        profileDataManager = new ProfileDataManager();
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }

    public void getUserData(final String userId, String friendlyUrl) {
        setProgressVisible(true);
        fetchUserDataFromDB(userId);
//        friendlyUrl = getFriendlyUrlFromDB(userId);
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
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUserDataFromDB(String userId) {
        user = profileDataManager.fetchUserData(userId, user);
        userDataListener.showUserProfile();
    }

//    public String getFriendlyUrlFromDB(String userId) {
//        return profileDataManager.getFriendlyUrlOfUser(userId);
//    }

    public void onToggleFollowing(User user) {
        accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);

        if (accessToken.isEmpty() || accessToken == null) {
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.follow_login_msg));
        } else if (user != null) {
            String userId = user.get_id();
            user.setFollowByMe(!user.isFollowByMe());
            if (user.isFollowByMe()) {
                User.getInstance().getFollowing().add(user.get_id());
                user.getFollowers().add(User.getInstance().get_id());
            } else {
                User.getInstance().getFollowing().remove(user.get_id());
                user.getFollowers().remove(User.getInstance().get_id());
            }
            toggleFollowing(userId, user);
            userDataListener.followToggeled();
        }
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
                    AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.follow_success));
                } else {
                    AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.unfollow_success));
                }
                User.getInstance().setRefreshRequired(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                user.setFollowByMe(!followByMe);
                if (followByMe) {
                    user.getFollowers().remove(User.getInstance().get_id());
                } else {
                    user.getFollowers().add(User.getInstance().get_id());
                }
                userDataListener.followToggeled();
            }

            @Override
            public void onError(Object response) {
                user.setFollowByMe(!followByMe);
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                if (followByMe) {
                    user.getFollowers().remove(User.getInstance().get_id());
                } else {
                    user.getFollowers().add(User.getInstance().get_id());
                }
                userDataListener.followToggeled();
            }
        });
    }

    private void setUpApiCall() {
        String token = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        accessToken = Constants.AUTHORIZATION_BEARER + token;
    }
}
