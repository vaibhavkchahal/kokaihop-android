package com.kokaihop.recipedetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.cookbooks.CookbookDataChangedListener;
import com.kokaihop.cookbooks.CookbooksApiHelper;
import com.kokaihop.cookbooks.CookbooksDataManager;
import com.kokaihop.cookbooks.model.AddToCookbookRequest;
import com.kokaihop.cookbooks.model.CookbookName;
import com.kokaihop.cookbooks.model.RemoveFromCookbookRequest;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.home.CookbookUpdateEvent;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.ProfileApiHelper;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.InputDialog;
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
    private boolean isDownloading = true, removed = false;
    private String userId, friendlyUrl, accessToken;
    private ProfileDataManager profileDataManager;
    private RecipeDataManager recipeDataManager;
    private CookbooksDataManager cookbooksDataManager;
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
        recipeDataManager = new RecipeDataManager();
        cookbooksDataManager = new CookbooksDataManager();
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

            });
        }

    }

    public void createNewCookbook() {
        GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.cookbook_category), context.getString(R.string.create_cookbook_action));

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
        setProgressVisible(true);
        String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        new CookbooksApiHelper().createCookbook(accessToken, new CookbookName(cookbookName), new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.cookbook_category), context.getString(R.string.created_cookbook_action));
                setDownloading(true);
                EventBus.getDefault().postSticky(new CookbookUpdateEvent("refreshCookbook"));
                getCookbooksOfUser(0);
                setProgressVisible(false);
                AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.cookbook_created));
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);

            }

        });
    }

    public void fetchCookbooksFromDB() {
        cookbooks = profileDataManager.getCookbooks(userId);
        User.getInstance().getCookbooks().clear();
        User.getInstance().getCookbooks().addAll(cookbooks);
        displayCookbooks();
    }

    private void displayCookbooks() {
        ((AddToCookbookFragment) fragment).displayCookbooks(cookbooks);
    }

    public void addToCookbook(final Cookbook cookbook, final String recipeId) {
        setProgressVisible(true);
        AddToCookbookRequest addToCookbookRequest = new AddToCookbookRequest(recipeId, cookbook.get_id());
        new CookbooksApiHelper().addToCookbook(accessToken, addToCookbookRequest, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(recipeId);
                if (cookbooksDataManager.insertRecipeIntoCookbook(recipeRealmObject, friendlyUrl, cookbook.getFriendlyUrl())) {
                    cookbook.setTotal(cookbook.getTotal() + 1);
                }
                recipeDataManager.updateIsFavoriteInDB(true, recipeRealmObject);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("favorite", true);
                ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                setProgressVisible(false);
                AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.recipe_added_to_cookbook));
                RecipeDataManager dataManager = new RecipeDataManager();
                RecipeRealmObject recipe = dataManager.fetchRecipe(recipeId);
                dataManager.updateIsFavoriteInDB(true, recipe);
                if (recipe.getCounter() != null) {
                    dataManager.updateLikesCount(recipe, recipe.getCounter().getLikes());
                }
                EventBus.getDefault().postSticky(new CookbookUpdateEvent("refreshCookbook"));
                EventBus.getDefault().postSticky(recipe);

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
                cookbook.setContains(!cookbook.isContains());
            }

        });
    }

    public void removeFromCookbook(final Cookbook cookbook, final String recipeId, final int position) {
        setProgressVisible(true);
        RemoveFromCookbookRequest removeFromCookbookRequest = new RemoveFromCookbookRequest(cookbook.get_id(), new String[]{recipeId});
        new CookbooksApiHelper().removeFromCookbook(accessToken, removeFromCookbookRequest, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.recipe_removed_from_cookbook));
                cookbook.setTotal(cookbook.getTotal() - 1);
                setProgressVisible(false);
                if (fragment instanceof CookbookDataChangedListener) {
                    ((CookbookDataChangedListener) fragment).updateList(position);

                }
                RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(recipeId);
                cookbooksDataManager.removeRecipeFromCookbook(recipeRealmObject, friendlyUrl, cookbook.get_id());
                EventBus.getDefault().postSticky(new CookbookUpdateEvent("refreshCookbook"));

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
                cookbook.setContains(!cookbook.isContains());
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
