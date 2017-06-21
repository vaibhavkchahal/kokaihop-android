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
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class ShowCommentsViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 25;
    private final String TYPE_FILTER = "RECIPE_COMMENT";
    private RecipeDataManager recipeDataManager;
    private String recipeID;
    private List<CommentRealmObject> commentsList = new ArrayList<>();
    private CommentDatasetListener commentListener;
    private long totalCommentCount;

    public long getTotalCommentCount() {
        return totalCommentCount;
    }

    public List<CommentRealmObject> getCommentsList() {
        return commentsList;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public int getMax() {
        return max;
    }

    public ShowCommentsViewModel(String recipeId, CommentDatasetListener dataSetListener) {
        this.recipeID = recipeId;
        recipeDataManager = new RecipeDataManager();
        this.commentListener = dataSetListener;
        fetchCommentsFromDB();
        fetchCommentFromServer(getOffset(), getMax(), true);
    }

    public void fetchCommentFromServer(int offset, int max, boolean progressVisibility) {
        setProgressVisible(progressVisibility);
        CommentRequestParams requestParams = new CommentRequestParams(offset, max, recipeID, TYPE_FILTER);
        new CommentsApiHelper().fetchCommentsList(requestParams, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject json = new JSONObject(responseBody.string());
                    JSONArray commentsJSONArray = json.getJSONArray("comments");
                    recipeDataManager.updateRecipeCommentList(recipeID, commentsJSONArray);
                    fetchCommentsFromDB();
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

    private void fetchCommentsFromDB() {
        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
        commentsList.clear();
        commentsList.addAll(recipeRealmObject.getComments());
        totalCommentCount = recipeRealmObject.getCounter().getComments();
        commentListener.onUpdateCommentsList();
    }


    // post comment after checking user authentication.
    public void postComment(View view, EditText editText) {
        Context context = view.getContext();
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_comment_message));
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
                            recipeDataManager.insertCommentRealmObject(recipeID, commentJsonObject);
                            fetchCommentsFromDB();
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
        requestParams.setReplyId(null);
        return requestParams;
    }

    @Override
    protected void destroy() {
    }

    public interface CommentDatasetListener {
        void onUpdateCommentsList();
    }

    public void onBackPressed(View view) {
        ((Activity) (view.getContext())).finish();
    }
}
