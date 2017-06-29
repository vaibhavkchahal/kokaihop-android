package com.kokaihop.userprofile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.signup.SignUpActivity;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.cookbooks.CookbooksApiHelper;
import com.kokaihop.cookbooks.model.CookbookName;
import com.kokaihop.home.MyCookbooksFragment;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
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

public class CookbooksViewModel extends BaseViewModel {

    private final Context context;
    private int offset, max, totalCount;
    private boolean isDownloading = true;
    private String userId;
    private ProfileDataManager profileDataManager;
    private Fragment fragment;
    User user;

    public CookbooksViewModel(Fragment fragment, Context context, String userId) {
        this.fragment = fragment;
        this.userId = userId;
        this.context = context;
        profileDataManager = new ProfileDataManager();
        if (fragment instanceof CookbooksFragment) {
            this.user = User.getOtherUser();
        } else {
            this.user = User.getInstance();
        }
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
                        profileDataManager.insertOrUpdateCookbooksUsingJSON(recipes, getUserId());
                        setTotalCount(jsonObject.getInt("total"));
                        fetchCookbooksFromDB();
                        showUserProfile();
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
        user.getCookbooks().clear();
        user.getCookbooks().addAll(cookbooks);
        showUserProfile();
    }

    private void showUserProfile() {
        if (fragment instanceof CookbooksFragment) {
            ((CookbooksFragment) fragment).showUserProfile();
        } else {
            ((MyCookbooksFragment) fragment).showUserProfile();
        }
    }

    public void createNewCookbook() {
        final Dialog dialog = new Dialog(fragment.getContext());
        dialog.setContentView(R.layout.dialog_new_cookbook);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.findViewById(R.id.create_cookbbok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String name = ((EditText) dialog.findViewById(R.id.cookbook_name)).getText().toString();
                createCookbook(name);
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void createCookbook(String cookbookName)
    {
        String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context,Constants.ACCESS_TOKEN);
        new CookbooksApiHelper().createCookbook(accessToken, new CookbookName(cookbookName), new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(context, R.string.cookbook_created, Toast.LENGTH_SHORT).show();
                setDownloading(true);
                getCookbooksOfUser(0);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, "Failure " +  R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, "Error " +  R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void showSignUpScreen() {
        context.startActivity(new Intent(context, SignUpActivity.class));
    }
}
