package com.kokaihop.comments;

import android.content.Context;
import android.content.Intent;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.utility.Constants;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class CommentsHandler {

    public void openCommentsScreen(Context context, String recipeId, String friendlyUrl) {
        Intent intent = new Intent(context, ShowAllCommentsActivity.class);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        intent.putExtra(Constants.FRIENDLY_URL, friendlyUrl);
        context.startActivity(intent);
    }

    public void openReplyScreen(Context context, String commentId, String recipeId) {
        Intent intent = new Intent(context, ReplyCommentActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("recipeId", recipeId);
        context.startActivity(intent);
    }

    public String checkOnReplyEventText(Context context, String comenterName, int replyCount) {
        String text;
        if ((replyCount - 1) > 0) {
            text = comenterName + context.getString(R.string.space_character) + context.getString(R.string.text_and) + (replyCount - 1) + context.getString(R.string.replied_text);
        } else {
            text = comenterName + context.getString(R.string.space_character) + context.getString(R.string.replied_text);
        }
        return text;
    }
}
