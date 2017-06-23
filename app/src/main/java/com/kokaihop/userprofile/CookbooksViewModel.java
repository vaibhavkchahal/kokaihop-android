package com.kokaihop.userprofile;

import android.content.Context;
import android.content.Intent;

import com.kokaihop.authentication.signup.SignUpActivity;
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
    private String userId;
    private ProfileDataManager profileDataManager;
    private CookbooksFragment fragment;
    User user;

    public CookbooksViewModel(CookbooksFragment fragment, Context context, String userId) {
        this.fragment = fragment;
        this.userId = userId;
        this.context = context;
        profileDataManager = new ProfileDataManager();
        this.user = User.getOtherUser();
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
                        profileDataManager.insertOrUpdateCookbooksUsingJSON(recipes, getUserId());
                        setTotalCount(jsonObject.getInt("count"));
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
                    fragment.showUserProfile();
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    setProgressVisible(false);
                    fragment.showUserProfile();
                }
            });
        }

    }

    private void fetchCookbooksFromDB() {
        ArrayList<Cookbook> cookbooks = profileDataManager.getCookbooks(userId);
        user.getCookbooks().clear();
        user.getCookbooks().addAll(cookbooks);
        fragment.showUserProfile();

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

    public void showSignUpScreen(){
        context.startActivity(new Intent(context, SignUpActivity.class));
    }
}
