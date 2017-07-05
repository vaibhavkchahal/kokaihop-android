package com.kokaihop.recipedetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.cookbooks.CookbooksApiHelper;
import com.kokaihop.cookbooks.model.AddToCookbookRequest;
import com.kokaihop.cookbooks.model.CookbookName;
import com.kokaihop.cookbooks.model.RemoveFromCookbookRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.ProfileApiHelper;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 20/6/17.
 */

public class AddToCookbookViewModel extends BaseViewModel {

    private final Context context;
    private int offset, max, totalCount;
    private boolean isDownloading = true;
    private String userId, friendlyUrl, accessToken;
    private ProfileDataManager profileDataManager;
    private Fragment fragment;
    private User user;
    private ArrayList<Cookbook> cookbooks;

    public AddToCookbookViewModel(Fragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        profileDataManager = new ProfileDataManager();
        cookbooks = new ArrayList<>();
        max = 20;
        this.userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        this.friendlyUrl = SharedPrefUtils.getSharedPrefStringData(context, Constants.FRIENDLY_URL);
        accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
    }


    public void getCookbooksOfUser(final int offset) {
        setProgressVisible(false);
        fetchCookbooksFromDB();
//        final String userId = "56387aa81e443c0300c5a4b5";

        setOffset(offset);
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
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if (getOffset() + getMax() >= getTotalCount()) {
                        setDownloading(false);
                    }
                    fetchCookbooksFromDB();
                    setProgressVisible(false);
                }

                @Override
                public void onFailure(String message) {
                    setDownloading(false);
                    displayCookbooks();
                    setProgressVisible(false);
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    displayCookbooks();
                    setProgressVisible(false);
                }
            });
        }

    }

    public void createNewCookbook() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_new_cookbook);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    //    API call for the new cookbook created by user.
    public void createCookbook(String cookbookName) {
        setProgressVisible(true);
        String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        new CookbooksApiHelper().createCookbook(accessToken, new CookbookName(cookbookName), new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(context, R.string.cookbook_created, Toast.LENGTH_SHORT).show();
                setDownloading(true);
                EventBus.getDefault().postSticky("updateCookbook");
                getCookbooksOfUser(0);
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, "Failure " + R.string.something_wrong, Toast.LENGTH_SHORT).show();
                setProgressVisible(false);

            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, "Error " + R.string.something_wrong, Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }
        });
    }

    public void fetchCookbooksFromDB() {
        cookbooks = profileDataManager.getCookbooks(userId);
        User.getOtherUser().getCookbooks().clear();
        User.getOtherUser().getCookbooks().addAll(cookbooks);
        displayCookbooks();
    }

    private void displayCookbooks() {
        ((AddToCookbookFragment) fragment).displayCookbooks(cookbooks);
    }

    public void addToCookbook(final Cookbook cookbook, String recipeId) {
        setProgressVisible(true);
        AddToCookbookRequest addToCookbookRequest = new AddToCookbookRequest(recipeId, cookbook.get_id());
        new CookbooksApiHelper().addToCookbook(accessToken, addToCookbookRequest, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(context, R.string.recipe_added_to_cookbook, Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky("refreshRecipeDetail");
                cookbook.setTotal(cookbook.getTotal() + 1);
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }
        });
    }

    public void removeFromCookbook(final Cookbook cookbook, String recipeId) {
        setProgressVisible(true);
        RemoveFromCookbookRequest removeFromCookbookRequest = new RemoveFromCookbookRequest(cookbook.get_id(), new String[]{recipeId});
        new CookbooksApiHelper().removeFromCookbook(accessToken, removeFromCookbookRequest, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(context, R.string.recipe_removed_from_cookbook, Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky("refreshRecipeDetail");
                cookbook.setTotal(cookbook.getTotal() - 1);
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }
        });
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
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
