package com.kokaihop.userprofile;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class CookbooksViewModel extends BaseViewModel {

    private final Context context;
    private int offset, max, totalCount;
    private boolean isDownloading = true;
    private String userId, friendlyUrl;
    private ProfileDataManager profileDataManager;
    private Fragment fragment;
    private User user;
    private ArrayList<Cookbook> cookbooks;

    public CookbooksViewModel(Fragment fragment, Context context, String userId, String friendlyUrl, User user) {
        this.fragment = fragment;
        this.userId = userId;
        this.context = context;
        profileDataManager = new ProfileDataManager();
        this.user = user;
        cookbooks = new ArrayList<>();
        max = 20;
        this.friendlyUrl = friendlyUrl;
    }


    public void getCookbooksOfUser(final int offset) {
        fetchCookbooksFromDB();
//        final String userId = "56387aa81e443c0300c5a4b5";

        setOffset(offset);
        setProgressVisible(true);
        if (isDownloading) {
            new ProfileApiHelper().getCookbooksOfUser(getUserId(), getOffset(), getMax(), new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {

                    ResponseBody responseBody = (ResponseBody) response;
                    try {
                        final JSONObject jsonObject = new JSONObject(responseBody.string());
                        JSONArray recipes = jsonObject.getJSONArray("recipeCollections");
                        profileDataManager.insertOrUpdateCookbooksUsingJSON(recipes, getUserId(), friendlyUrl);
                        setTotalCount(jsonObject.getInt("total"));
                        fetchCookbooksFromDB();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (getOffset() + getMax() >= getTotalCount()) {
                        setDownloading(false);
                    }
                    setProgressVisible(false);
                    fetchCookbooksFromDB();
                }

                @Override
                public void onFailure(String message) {
                    setDownloading(false);
                    setProgressVisible(false);
                    showUserProfile();
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    setProgressVisible(false);
                    showUserProfile();
                }
            });
        }

    }

    public void fetchCookbooksFromDB() {
        cookbooks = profileDataManager.getCookbooks(userId);
        user.getCookbooks().clear();
        user.getCookbooks().addAll(cookbooks);
        showUserProfile();
    }

    private void showUserProfile() {
        if (fragment.isVisible())
            ((CookbooksFragment) fragment).showUserProfile(cookbooks);
    }

    @Override
    protected void destroy() {

    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
