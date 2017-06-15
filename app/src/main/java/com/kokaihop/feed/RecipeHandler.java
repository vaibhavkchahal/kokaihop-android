package com.kokaihop.feed;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipedetail.RecipeDetailActivity;
import com.kokaihop.userprofile.HistoryDataManager;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;

import static com.kokaihop.utility.SharedPrefUtils.getSharedPrefStringData;

/**
 * Created by Vaibhav Chahal on 22/5/17.
 */

public class RecipeHandler {
    private int recipePosition = -1;

    public void setRecipePosition(int position) {
        recipePosition = position;
    }

    public void onCheckChangeRecipe(CheckBox checkBox, Recipe recipe) {
        String accessToken = SharedPrefUtils.getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            Context context = checkBox.getContext();
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_like_message));
        } else {
            performOperationOncheck(checkBox, recipe);
        }
    }

    private void performOperationOncheck(CheckBox checkBox, Recipe recipe) {
        updateSatusInDB(checkBox.isChecked(), recipe);
        updatelikeStatusOnServer(checkBox, recipe);
    }

    private void updateSatusInDB(boolean checked, Recipe recipe) {
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        long likes = 0;
        likes = Long.valueOf(recipe.getLikes());
        if (checked) {
            likes = likes + 1;
        } else {
            if (likes != 0) {
                likes = likes - 1;
            }
        }
        recipeDataManager.updateIsFavoriteInDB(checked, recipe);
        recipeDataManager.updateLikesCount(recipe, likes);
        recipe.setLikes(String.valueOf(likes));
        recipe.setFavorite(checked);
    }

    public void updatelikeStatusOnServer(final CheckBox checkBox, final Recipe recipe) {
        final Context context = checkBox.getContext();
        String accessToken = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                if (context.getClass().getSimpleName().equals(context.getString(R.string.recipe_detail_activity_title))) {
                    EventBus.getDefault().postSticky(new RecipeDetailPostEvent(recipe, recipePosition));
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                revertLikeStatus(checkBox, recipe);
            }

            @Override
            public void onError(Object response) {
                revertLikeStatus(checkBox, recipe);

            }
        });
    }

    private void revertLikeStatus(CheckBox checkBox, Recipe recipe) {
        updateSatusInDB(!checkBox.isChecked(), recipe);
    }

    public void openRecipeDetail(View view, String recipeId, int position) {
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new HistoryDataManager().updateHistory(recipeId);
        intent.putExtra("recipeId", recipeId);
        intent.putExtra("recipePosition", position);
        view.getContext().startActivity(intent);
    }
}
