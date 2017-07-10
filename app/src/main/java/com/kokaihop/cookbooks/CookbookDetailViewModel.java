package com.kokaihop.cookbooks;

import android.app.Activity;
import android.content.Context;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.android.databinding.library.baseAdapters.BR;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.cookbooks.model.RenameCookbookRequest;
import com.kokaihop.feed.Recipe;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipedetail.AddToCookbookViewModel;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.ConfirmationDialog;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.InputDialog;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Rajendra Singh on 27/6/17.
 */

public class CookbookDetailViewModel extends BaseViewModel {


    private Fragment fragment;
    private Context context;
    private int offset, max, totalRecipes;
    private boolean isDownloading;
    private CookbooksDataManager dataManager;
    private User user;
    private String accessToken, cookbookId, cookbookFriendlyUrl,cookbookTitle;

    public CookbookDetailViewModel(Fragment fragment, Context context, User user) {
        this.fragment = fragment;
        this.context = context;
        this.user = user;
        isDownloading = true;
        dataManager = new CookbooksDataManager();
        max = 20;
        accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
    }

    public void getRecipesOfCookbook(final String cookbookFriendlyUrl, final String userFriendlyUrl, int offset) {
        cookbookId = dataManager.getIdOfCookbook(cookbookFriendlyUrl);
        setProgressVisible(true);
        this.cookbookFriendlyUrl = cookbookFriendlyUrl;
        fetchRecipesOfCookbooksFromDB(userFriendlyUrl, cookbookFriendlyUrl);
        setOffset(offset);
        if (isDownloading) {
            new CookbooksApiHelper().getRecipesOfCookbook(cookbookFriendlyUrl, userFriendlyUrl, getOffset(), getMax(), new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {

                    ResponseBody responseBody = (ResponseBody) response;
                    if (responseBody != null) {
                        try {
                            final JSONObject jsonObject = new JSONObject(responseBody.string());
                            JSONArray recipes = jsonObject.getJSONArray("recipes");
                            dataManager.insertOrUpdateRecipeIntoCookbooks(recipes, userFriendlyUrl, cookbookFriendlyUrl);
                            setTotalRecipes(jsonObject.getInt("total"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setProgressVisible(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                            setProgressVisible(false);
                        }

                        if (getOffset() + getMax() >= getTotalRecipes()) {
                            setDownloading(false);
                        }
                        setProgressVisible(false);
                        fetchRecipesOfCookbooksFromDB(userFriendlyUrl, cookbookFriendlyUrl);
                    }
                }

                @Override
                public void onFailure(String message) {
                    Logger.e("Error", "Recipe not found");
                    setDownloading(false);
                    setProgressVisible(false);
                    ((CookbookDetailFragment) fragment).showCookbookDetails();
                }

                @Override
                public void onError(Object response) {
                    setDownloading(false);
                    setProgressVisible(false);
                    ((CookbookDetailFragment) fragment).showCookbookDetails();
                }
            });
        }

    }

    public void deleteCookbook() {
        String msg = context.getString(R.string.this_will_permanently_delete);
        if (totalRecipes > 0) {
            msg += totalRecipes
                    + context.getString(R.string.recipes_in_the_cookbook);
        } else {
            msg += context.getString(R.string.the_cookbook);
        }

        final ConfirmationDialog dialog = new ConfirmationDialog(fragment.getActivity(),
                context.getString(R.string.are_you_sure_you_want_to_delete_this_cookbook),
                msg,
                context.getString(R.string.delete),
                context.getString(R.string.cancel));

        dialog.getConfirmPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCookbookOfUser();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deleteCookbookOfUser() {
        setProgressVisible(true);
        new CookbooksApiHelper().deleteCookbook(accessToken, cookbookId, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(context, context.getString(R.string.cookbook_deleted), Toast.LENGTH_SHORT).show();
                dataManager.deleteCookbook(cookbookFriendlyUrl);
                user.setCookbooks(new ProfileDataManager().getCookbooks(SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID)));
                EventBus.getDefault().postSticky("refreshCookbook");
                ((Activity) context).finish();
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                setProgressVisible(false);
            }
        });
    }

    //    get the recipes list from the database.
    public void fetchRecipesOfCookbooksFromDB(String userFriendlyUrl, String cookbookFriendlyUrl) {
        ArrayList<Recipe> recipeList = dataManager.getRecipesOfCookbook(userFriendlyUrl, cookbookFriendlyUrl);
        user.getRecipesList().clear();
        user.getRecipesList().addAll(recipeList);
        ((CookbookDetailFragment) fragment).showCookbookDetails();
    }

    public void renameCookbook() {
        final InputDialog dialog = new InputDialog(fragment.getContext());
        dialog.setupDialog(
                context.getString(R.string.rename_cookbook),
                context.getString(R.string.cookbook_name),
                context.getString(R.string.rename),
                context.getString(R.string.cancel));

        dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String name = ((EditText) dialog.findViewById(R.id.dialog_text)).getText().toString();
                renameCookbookApiCall(name);
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

    public void renameCookbookApiCall(final String name) {
        RenameCookbookRequest request = new RenameCookbookRequest(name, cookbookId);
        new CookbooksApiHelper().renameCookbook(accessToken, cookbookId, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.cookbook_renamed));
                setCookbookTitle(name);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, "Failure " + R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
                Toast.makeText(context, "Failure " + R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void removeRecipeFromCookbook(final String recipeId, final int position) {
        final ConfirmationDialog dialog = new ConfirmationDialog(context,
                context.getString(R.string.cookbook),
                context.getString(R.string.are_you_sure_you_want_to_delete_this_recipe),
                context.getString(R.string.delete),
                context.getString(R.string.cancel)
        );
        dialog.getConfirmPositive().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setProgressVisible(true);
                Cookbook cookbook = new Cookbook();
                cookbook.set_id(cookbookId);
                cookbook.setTotal(totalRecipes);
                new AddToCookbookViewModel(fragment, context).removeFromCookbook(cookbook, recipeId, position);
                setProgressVisible(false);
            }
        });
        dialog.show();
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTotalRecipes() {
        return totalRecipes;
    }

    public void setTotalRecipes(int totalRecipes) {
        this.totalRecipes = totalRecipes;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    @Bindable
    public String getCookbookTitle() {
        return cookbookTitle;
    }

    public void setCookbookTitle(String cookbookTitle) {
        this.cookbookTitle = cookbookTitle;
        notifyPropertyChanged(BR.cookbookTitle);
    }
}
