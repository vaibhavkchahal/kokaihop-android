package com.kokaihop.feed.maincourse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.CheckBox;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.database.Recipe;
import com.kokaihop.feed.FeedApiHelper;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
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
        String accessToken = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(checkBox.getContext(), Constants.ACCESS_TOKEN);
        RecipeLikeRequest request = new RecipeLikeRequest(recipe.get_id(), checkBox.isChecked());
        new FeedApiHelper().updateRecipeLike(accessToken, request, new IApiRequestComplete() {
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

}
