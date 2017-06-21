package com.kokaihop.recipedetail;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.utility.SharedPrefUtils.getSharedPrefStringData;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class RecipeRatingHandler {

    public RecipeRatingHandler(final RatingBar ratingBar, final RecipeDetailHeader recipeDetailHeader) {
        final Context context = ratingBar.getContext();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                    if (accessToken == null || accessToken.isEmpty()) {
                        ratingBar.setRating(recipeDetailHeader.getRating());
                        AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_rating_message));
                    } else {
                        updateRecipeRating(ratingBar, recipeDetailHeader);
                    }
                }
            }
        });
    }

    private void updateRecipeRating(final RatingBar ratingBar, final RecipeDetailHeader recipeDetailHeader) {
        String accessTokenBearer = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(ratingBar.getContext(), Constants.ACCESS_TOKEN);
        new RecipeDetailApiHelper().rateRecipe(accessTokenBearer, new RatingRequestParams(recipeDetailHeader.getRecipeId(), ratingBar.getRating()), new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                Context context = ratingBar.getContext();
                showRateSucessDialog(context, context.getString(R.string.rating_dialog_text) + ratingBar.getRating());
                ratingBar.setRating(recipeDetailHeader.getRating());
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ratingBar.getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
            }
        });
    }

    public static void showRateSucessDialog(final Context context, final String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rating_success_view);
        TextView textView = (TextView) dialog.findViewById(R.id.txtview_rating_sucess);
        textView.setText(message);
        dialog.show();
        hideDialogAfterTimeOut(dialog);
    }

    private static void hideDialogAfterTimeOut(final Dialog dialog) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }
}
