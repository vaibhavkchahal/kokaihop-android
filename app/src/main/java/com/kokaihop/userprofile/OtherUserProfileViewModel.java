package com.kokaihop.userprofile;

import android.content.Context;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;

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
    User otherUser;


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
        friendlyUrl =  getFriendlyUrlFromDB(userId);
        new ProfileApiHelper().getOtherUserData(accessToken, friendlyUrl, countryCode, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    jsonObject = jsonObject.getJSONObject("user");
                    new ProfileDataManager().insertOrUpdateUserDataUsingJSON(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
}
