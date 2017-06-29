package com.kokaihop.recipedetail;

import android.content.Context;
import android.content.Intent;

import com.kokaihop.userprofile.OtherUserProfileActivity;
import com.kokaihop.utility.Constants;

/**
 * Created by Vaibhav Chahal on 29/6/17.
 */

public class RecipeSpecificationHandler {

    public void openUserProfile(Context context, String userId, String friendlyUrl) {
        Intent i = new Intent(context, OtherUserProfileActivity.class);
        i.putExtra(Constants.USER_ID, userId);
        i.putExtra(Constants.FRIENDLY_URL, friendlyUrl);
        (context).startActivity(i);
    }
}
