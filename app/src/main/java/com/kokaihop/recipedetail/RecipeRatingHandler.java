package com.kokaihop.recipedetail;

import android.app.Activity;
import android.content.Context;
import android.widget.RatingBar;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.database.RatingRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;

import static com.kokaihop.utility.SharedPrefUtils.getSharedPrefStringData;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class RecipeRatingHandler {

    public RecipeRatingHandler(final RatingBar ratingBar, final RecipeDetailHeader recipeDetailHeader, final RecipeRealmObject recipe) {
        final Context context = ratingBar.getContext();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    String accessToken = getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                    if (accessToken == null || accessToken.isEmpty()) {
                        ratingBar.setRating(recipeDetailHeader.getRating());
                        AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_rating_message));
                    } else {
                        if (!recipeDetailHeader.getCreatorFriendlyUrl().equals(SharedPrefUtils.getSharedPrefStringData(context, Constants.FRIENDLY_URL))) {
                            updateRecipeRating(ratingBar, recipeDetailHeader, recipe);
                        } else {
                            ratingBar.setRating(recipeDetailHeader.getRating());
                        }
                    }
                }
            }
        });
    }

    private void updateRecipeRating(final RatingBar ratingBar, final RecipeDetailHeader recipeDetailHeader, final RecipeRealmObject recipe) {
        final int rating = (int) ratingBar.getRating();
        if (rating > 0) {
            final Context context = ratingBar.getContext();
            String accessTokenBearer = Constants.AUTHORIZATION_BEARER + getSharedPrefStringData(ratingBar.getContext(), Constants.ACCESS_TOKEN);
            new RecipeDetailApiHelper().rateRecipe(accessTokenBearer, new RatingRequestParams(recipeDetailHeader.getRecipeId(), rating), new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_rated_action));
                    Activity activity = (Activity) ratingBar.getContext();
                    ResponseBody responseBody = (ResponseBody) response;
                    RatingRealmObject ratingRealmObject = recipe.getRating();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    if (ratingRealmObject == null) {
                        ratingRealmObject = realm.createObject(RatingRealmObject.class);
                    }
                    try {
                        JSONObject ratingResponse = new JSONObject(responseBody.string());
                        ratingRealmObject.setAverage((float) ratingResponse.getDouble("average"));
                        ratingRealmObject.setRaters(ratingResponse.getInt("raters"));
                        ratingBar.setRating(ratingRealmObject.getAverage());
                        recipe.setRating(ratingRealmObject);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    realm.commitTransaction();
                    AppUtility.showAutoCancelMsgDialog(context, context.getString(R.string.rating_dialog_text) + " " + rating);
                    EventBus.getDefault().postSticky(recipe);
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(recipeDetailHeader.getRating());
                }

                @Override
                public void onError(Object response) {
                    ratingBar.setRating(recipeDetailHeader.getRating());
                }
            });
        } else {
            ratingBar.setRating(recipeDetailHeader.getRating());
        }
    }
}
