package com.kokaihop.comments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class ReplyCommentViewModel extends BaseViewModel {

    private final String TYPE_FILTER = "RECIPE_COMMENT";
    private RecipeDataManager recipeDataManager;
    private String commentID;
    private String recipeID;
    private List<CommentRealmObject> commentsList = new ArrayList<>();
    private CommentDatasetListener commentListener;
    public CommentRealmObject commentRealmObject;

    public List<CommentRealmObject> getCommentsList() {
        return commentsList;
    }

    public ReplyCommentViewModel(String recipeId, String commentId, CommentDatasetListener dataSetListener) {
        this.recipeID = recipeId;
        this.commentID = commentId;
        recipeDataManager = new RecipeDataManager();
        this.commentListener = dataSetListener;
        fetchCommentFromDB();
        fetchCommentFromServer(true);
    }

    public void fetchCommentFromServer(boolean progressVisibility) {
        setProgressVisible(progressVisibility);
        new CommentsApiHelper().fetchSingleCommentInfo(commentID, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject commentsJSONObject = new JSONObject(responseBody.string());
                    recipeDataManager.updateCommentRealmObject(commentsJSONObject);
                    fetchCommentFromDB();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);
            }
        });
    }

    private void fetchCommentFromDB() {
        commentRealmObject = recipeDataManager.fetchCopyOfComment(commentID);
        commentsList.clear();
        commentsList.add(commentRealmObject);
        commentsList.addAll(commentRealmObject.getPayload().getReplyEvents());
        commentListener.onUpdateComment();
    }


    // post comment after checking user authentication.
    public void postReplyOnComment(View view, EditText editText) {
        Context context = view.getContext();
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_reply_on_comment_message));
        } else {
            if (!editText.getText().toString().isEmpty()) {
                String bearerAccessToken = Constants.AUTHORIZATION_BEARER + accessToken;
                PostCommentRequestParams requestParams = getPostCommentRequestParams(editText);
                setProgressVisible(true);
                new CommentsApiHelper().postComment(bearerAccessToken, requestParams, new IApiRequestComplete() {
                    @Override
                    public void onSuccess(Object response) {
                        setProgressVisible(false);
                        ResponseBody responseBody = (ResponseBody) response;
                        try {
                            JSONObject commentJsonObject = new JSONObject(responseBody.string());
                            recipeDataManager.insertCommentReplyEvents(commentID, commentJsonObject);
                            fetchCommentFromDB();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                    }

                    @Override
                    public void onError(Object response) {
                        setProgressVisible(false);
                    }
                });
            } else {
                Toast.makeText(view.getContext(), R.string.empty_comment_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    private PostCommentRequestParams getPostCommentRequestParams(EditText editText) {
        PostCommentRequestParams requestParams = new PostCommentRequestParams();
        requestParams.setComment(editText.getText().toString());
        editText.setText("");
        requestParams.setType(TYPE_FILTER);
        requestParams.setTargetId(recipeID);
        requestParams.setReplyId(commentID);
        return requestParams;
    }

    @Override
    protected void destroy() {
    }

    public interface CommentDatasetListener {
        void onUpdateComment();
    }

    public void onBackPressed(View view) {
        ((Activity) (view.getContext())).finish();
    }
}
