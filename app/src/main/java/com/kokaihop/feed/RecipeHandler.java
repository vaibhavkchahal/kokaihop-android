package com.kokaihop.feed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.comments.ShowAllCommentsActivity;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipedetail.RecipeDetailActivity;
import com.kokaihop.search.SearchActivity;
import com.kokaihop.userprofile.HistoryDataManager;
import com.kokaihop.userprofile.OtherUserProfileActivity;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

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
        updateLikeCountInView(checkBox, recipe);
//        updateSatusInDB(checkBox.isChecked(), recipe);
        updatelikeStatusOnServer(checkBox, recipe);
    }

    private void updateLikeCountInView(CheckBox checkBox, RecipeRealmObject recipe) {
        long count = 0;
        Realm realm = Realm.getDefaultInstance();
        if (recipe.getCounter() != null) {
            count = recipe.getCounter().getLikes();
            if (checkBox.isChecked())
                count++;
            else if (count > 0) {
                count--;
            }
            realm.beginTransaction();
            recipe.getCounter().setLikes(count);
            realm.commitTransaction();
        }
        checkBox.setText(String.valueOf(count));

    }

    private void updateSatusInDB(boolean checked, RecipeRealmObject recipe) {
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        long likes = 0;
        if (recipe.getCounter() != null) {
            likes = Long.valueOf(recipe.getCounter().getLikes());
        }
//        if (checked) {
//            likes = likes + 1;
//        } else {
//            if (likes != 0) {
//                likes = likes - 1;
//            }
//        }
        recipeDataManager.updateIsFavoriteInDB(checked, recipe);
        recipeDataManager.updateLikesCount(recipe, likes);
        if (recipe.getCounter() != null) {
            if (!recipe.getCounter().isManaged()) {
                recipe.getCounter().setLikes(likes);
                recipe.setFavorite(checked);
            }
        }


    }

    public void updatelikeStatusOnServer(final CheckBox checkBox, final RecipeRealmObject recipe) {
        final Context context = checkBox.getContext();
        String accessToken = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
//                updateLikeCountInView(checkBox, recipe);
                updateSatusInDB(checkBox.isChecked(), recipe);
                String gaEventAction = checkBox.isChecked() ? context.getString(R.string.recipe_favourtized_action) : context.getString(R.string.recipe_unfavorited_action);
                Activity activity = (Activity) checkBox.getContext();
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), gaEventAction);
                String contextName = context.getClass().getSimpleName();
                if (contextName.equals(RecipeDetailActivity.class.getSimpleName()) || contextName.equals(SearchActivity.class.getSimpleName())) {
                    EventBus.getDefault().postSticky(recipe);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                revertLikeStatusInDB(checkBox, recipe);
                checkBox.setChecked(!checkBox.isChecked());
                updateCheckboxImage(checkBox.isChecked(), checkBox);
                updateLikeCountInView(checkBox, recipe);

            }

        });
    }

    private void updateCheckboxImage(boolean isChecked, CheckBox checkBox) {
        Drawable drawable;
        if (checkBox.isChecked()) {
            drawable = ResourcesCompat.getDrawable(checkBox.getContext().getResources(), R.drawable.ic_like_sm, null);
        } else {
            drawable = ResourcesCompat.getDrawable(checkBox.getContext().getResources(), R.drawable.ic_unlike_sm, null);
        }
        checkBox.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }

    private void revertLikeStatusInDB(CheckBox checkBox, RecipeRealmObject recipe) {
        updateSatusInDB(!checkBox.isChecked(), recipe);
    }

    public void openRecipeDetail(View view, String recipeId, int position, String imageWidth, String imageHeight) {
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new HistoryDataManager().updateHistory(recipeId);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        intent.putExtra(Constants.RECIPE_POSITION, position);
        intent.putExtra(Constants.IMAGE_WIDTH, imageWidth);
        intent.putExtra(Constants.IMAGE_HEIGHT, imageHeight);
        view.getContext().startActivity(intent);
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

    public void openRecipeDetailUsingFriendlyUrl(View view, String friendlyUrl, String imageWidth, String imageHeight) {
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.IMAGE_WIDTH, imageWidth);
        intent.putExtra(Constants.IMAGE_HEIGHT, imageHeight);
        intent.putExtra(Constants.FRIENDLY_URL, friendlyUrl);
        view.getContext().startActivity(intent);
    }
}
