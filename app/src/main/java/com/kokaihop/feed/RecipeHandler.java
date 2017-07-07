package com.kokaihop.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.comments.ShowAllCommentsActivity;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipedetail.RecipeDetailActivity;
import com.kokaihop.userprofile.HistoryDataManager;
import com.kokaihop.userprofile.OtherUserProfileActivity;
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

    public void onCheckChangeRecipe(CheckBox checkBox, RecipeRealmObject recipe) {
        String accessToken = SharedPrefUtils.getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            Context context = checkBox.getContext();
            checkBox.setChecked(false);
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_like_message));
        } else {
            performOperationOncheck(checkBox, recipe);
        }
    }

    private void performOperationOncheck(CheckBox checkBox, RecipeRealmObject recipe) {
        updateCheckboxImage(checkBox.isChecked(), checkBox);
        updatelikeStatusOnServer(checkBox, recipe);
        updateLikeCountInView(checkBox, recipe);
        updateSatusInDB(checkBox.isChecked(), recipe);
    }

    private void updateLikeCountInView(CheckBox checkBox, RecipeRealmObject recipe) {
        checkBox.setText(String.valueOf(recipe.getCounter().getLikes()));

    }

    private void updateSatusInDB(boolean checked, RecipeRealmObject recipe) {
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        long likes = 0;
        likes = Long.valueOf(recipe.getCounter().getLikes());
        if (checked) {
            likes = likes + 1;
        } else {
            if (likes != 0) {
                likes = likes - 1;
            }
        }
        recipeDataManager.updateIsFavoriteInDB(checked, recipe);
        recipeDataManager.updateLikesCount(recipe, likes);
        if (!recipe.getCounter().isManaged()) {
            recipe.getCounter().setLikes(likes);
            recipe.setFavorite(checked);
        }


    }

    public void updatelikeStatusOnServer(final CheckBox checkBox, final RecipeRealmObject recipe) {
        final Context context = checkBox.getContext();
        String accessToken = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                updateLikeCountInView(checkBox, recipe);
                if (context.getClass().getSimpleName().equals(context.getString(R.string.recipe_detail_activity_title))) {
                    EventBus.getDefault().postSticky(new RecipeDetailPostEvent(recipe, recipePosition));
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                revertLikeStatusInDB(checkBox, recipe);
                updateCheckboxImage(!checkBox.isChecked(), checkBox);
                updateLikeCountInView(checkBox, recipe);

            }

            @Override
            public void onError(Object response) {
                revertLikeStatusInDB(checkBox, recipe);
                updateCheckboxImage(!checkBox.isChecked(), checkBox);
                updateLikeCountInView(checkBox, recipe);

            }
        });
    }

    private void updateCheckboxImage(boolean isChecked, CheckBox checkBox) {
        Drawable drawable;
        if (isChecked) {
            drawable = ResourcesCompat.getDrawable(checkBox.getContext().getResources(), R.drawable.ic_like_sm, null);
        } else {
            drawable = ResourcesCompat.getDrawable(checkBox.getContext().getResources(), R.drawable.ic_unlike_sm, null);

        }
        checkBox.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }

    private void revertLikeStatusInDB(CheckBox checkBox, RecipeRealmObject recipe) {
        updateSatusInDB(!checkBox.isChecked(), recipe);
    }

    public void openRecipeDetail(View view, String recipeId, int position) {
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new HistoryDataManager().updateHistory(recipeId);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        intent.putExtra(Constants.RECIPE_POSITION, position);
        view.getContext().startActivity(intent);
    }

    public void openUserProfile(Context context, String userId, String friendlyUrl) {
        Intent i = new Intent(context, OtherUserProfileActivity.class);
        i.putExtra(Constants.USER_ID, userId);
        i.putExtra(Constants.FRIENDLY_URL, friendlyUrl);
        (context).startActivity(i);
    }

    public void openCommentsScreen(Context context, String recipeId, String friendlyUrl) {
        if (recipeId != null) {
            Intent intent = new Intent(context, ShowAllCommentsActivity.class);
            intent.putExtra(Constants.RECIPE_ID, recipeId);
            context.startActivity(intent);
        } else {
            AppUtility.showToastMessage(context, context.getString(R.string.something_went_wrong));
        }
    }

    public void openRecipeDetailUsingFriendlyUrl(View view, String friendlyUrl) {
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.FRIENDLY_URL, friendlyUrl);
        view.getContext().startActivity(intent);
    }
}
