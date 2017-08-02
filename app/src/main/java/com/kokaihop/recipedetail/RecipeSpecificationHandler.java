package com.kokaihop.recipedetail;

import android.content.Context;
import android.content.Intent;

import com.kokaihop.search.SearchActivity;
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


    public void searchByCourseTag(Context context, RecipeSpecifications recipeSpecifications) {
        Intent i = new Intent(context, SearchActivity.class);
        i.putExtra(Constants.COURSE_NAME, recipeSpecifications.getCategory1());
        i.putExtra(Constants.COURSE_FRIENDLY_URL, recipeSpecifications.getCategory1FriendlyUrl());
        (context).startActivity(i);
    }

    public void searchByCuisineTag(Context context, RecipeSpecifications recipeSpecifications) {
        Intent i = new Intent(context, SearchActivity.class);
        i.putExtra(Constants.CUISINE_NAME, recipeSpecifications.getCategory2());
        i.putExtra(Constants.CUISINE_FRIENDLY_URL, recipeSpecifications.getCategory2FriendlyUrl());

        (context).startActivity(i);
    }

    public void searchByMethodTag(Context context, RecipeSpecifications recipeSpecifications) {
        Intent i = new Intent(context, SearchActivity.class);
        i.putExtra(Constants.METHOD_NAME, recipeSpecifications.getCategory3());
        i.putExtra(Constants.METHOD_FRIENDLY_URL, recipeSpecifications.getCategory3FriendlyUrl());

        (context).startActivity(i);
    }
}
