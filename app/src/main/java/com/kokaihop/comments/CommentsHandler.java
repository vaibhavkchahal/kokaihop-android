package com.kokaihop.comments;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class CommentsHandler {

    public void openCommentsScreen(Context context, String recipeId) {
        Intent intent = new Intent(context, ShowAllCommentsActivity.class);
        intent.putExtra("recipeId", recipeId);
        context.startActivity(intent);
    }
}