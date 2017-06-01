package com.kokaihop.feed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipe.recipedetail.RecipeDetailActivity;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.utility.SharedPrefUtils.getSharedPrefStringData;

/**
 * Created by Vaibhav Chahal on 22/5/17.
 */

public class RecipeHandler {

    public void onCheckChangeRecipe(CheckBox checkBox, Recipe recipe) {
        String accessToken = SharedPrefUtils.getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            showDialog(checkBox);
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
        String accessToken = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken, request, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(checkBox.getContext(), message, Toast.LENGTH_SHORT).show();
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


    public void showDialog(final CheckBox checkBox) {
        final Context context = checkBox.getContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.members_area);
        dialog.setMessage(R.string.login_like_message);
        dialog.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isComingFromLike", true);
                context.startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openRecipeDetail(View view,String recipeId){
        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
        intent.putExtra("recipeId",recipeId);
        view.getContext().startActivity(intent);
    }

}
