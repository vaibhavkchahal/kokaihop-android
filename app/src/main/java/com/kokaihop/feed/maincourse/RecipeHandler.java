package com.kokaihop.feed.maincourse;

import android.util.Log;
import android.widget.CheckBox;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.database.Recipe;
import com.kokaihop.feed.FeedApiHelper;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Vaibhav Chahal on 22/5/17.
 */

public class RecipeHandler {

    public void onCheckChangeRecipe(CheckBox checkBox, Recipe recipe) {
        if (checkBox.isChecked()) {
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_sm, 0, 0, 0);
        } else {
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike_sm, 0, 0, 0);
        }
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        recipeDataManager.updateIsFavoriteInDB(checkBox.isChecked(), recipe);
        updatelikeStatusOnServer(checkBox, recipe);
    }

    public void updatelikeStatusOnServer(CheckBox checkBox, Recipe recipe) {

        String accessToken = Constants.AUTHORIZATION_BEARER+SharedPrefUtils.getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);

        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken,request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Log.i("success", ((RecipeLikeApiResponse) response).getId());
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onError(Object response) {
            }
        });
    }

}
