package com.kokaihop.cookbooks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.authentication.signup.SignUpActivity;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.cookbooks.model.CookbookName;
import com.kokaihop.cookbooks.model.CookbooksList;
import com.kokaihop.home.MyCookbooksFragment;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.ProfileApiHelper;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.InputDialog;
import com.kokaihop.utility.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class MyCookbooksViewModel extends BaseViewModel {

    private final Context context;
    private int offset, max, totalCount;
    private boolean isDownloading = true;
    private String userId, friendlyUrl;
    private ProfileDataManager profileDataManager;
    private Fragment fragment;
    private User user;

    public MyCookbooksViewModel(Fragment fragment, Context context, String userId, String friendlyUrl) {
        this.fragment = fragment;
        this.userId = userId;
        this.context = context;
        this.user = User.getInstance();
        profileDataManager = new ProfileDataManager();
        this.friendlyUrl = friendlyUrl;
        max = 20;
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
        ArrayList<Cookbook> cookbooks = profileDataManager.getCookbooks(userId);
        CookbooksList.getCookbooksList().getCookbooks().clear();
        CookbooksList.getCookbooksList().getCookbooks().addAll(cookbooks);
        showUserProfile();
    }

    private void showUserProfile() {
        ((MyCookbooksFragment) fragment).showUserProfile();
    }

    public void createNewCookbook() {

        GoogleAnalyticsHelper.trackEventAction(fragment.getActivity(), context.getString(R.string.cookbook_category), context.getString(R.string.create_cookbook_action));

        final InputDialog dialog = new InputDialog(fragment.getContext());
        dialog.setupDialog(
                context.getString(R.string.create_new_cookbook),
                context.getString(R.string.cookbook_name),
                context.getString(R.string.create),
                context.getString(R.string.cancel));

        dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) dialog.findViewById(R.id.dialog_text)).getText().toString();
                if (AppUtility.isEmptyString(name)) {
                    Toast.makeText(context, context.getString(R.string.empty_cookbook_msg), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    createCookbook(name.trim());
                }
            }
        });

        dialog.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //    API call for the new cookbook created by user.
    public void createCookbook(String cookbookName) {
        String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        new CookbooksApiHelper().createCookbook(accessToken, new CookbookName(cookbookName), new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                GoogleAnalyticsHelper.trackEventAction(fragment.getActivity(), context.getString(R.string.cookbook_category), context.getString(R.string.created_cookbook_action));

                AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.cookbook_created));
                setDownloading(true);
                getCookbooksOfUser(0);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void destroy() {

    }

    public void showLoginDialog() {
        AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.cookbook_login_msg));
    }

    public void showSignUpScreen() {
        context.startActivity(new Intent(context, SignUpActivity.class));
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