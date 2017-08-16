package com.kokaihop.comments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
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
import java.util.Map;
import java.util.WeakHashMap;

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
    Context context;

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
        this.context = ((Activity) dataSetListener);
    }

    public void fetchCommentFromServer(int offset, int max, boolean progressVisibility) {
        setProgressVisible(progressVisibility);
        Map<String, String> map = new WeakHashMap<>();
        map.put("max", String.valueOf(max));
        map.put("offset", String.valueOf(offset));
        map.put("typeFilter", TYPE_FILTER);
        map.put("recipeId", recipeID);
        new CommentsApiHelper().fetchCommentsList(map, new IApiRequestComplete() {
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
                Toast.makeText(context, R.string.check_intenet_connection, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void fetchCommentsFromDB() {
        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
        commentsList.clear();
        commentsList.addAll(recipeRealmObject.getComments());
        if (recipeRealmObject.getCounter() != null) {
            totalCommentCount = recipeRealmObject.getCounter().getComments();
        }
        commentListener.onUpdateCommentsList();
    }


    // post comment after checking user authentication.
    public void postComment(View view, EditText editText) {
        final Context context = view.getContext();
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            AppUtility.showLoginDialog(context, context.getString(R.string.members_area), context.getString(R.string.login_comment_message));
        } else {

            if (!AppUtility.isEmptyString(editText.getText().toString())) {
                String bearerAccessToken = Constants.AUTHORIZATION_BEARER + accessToken;
                PostCommentRequestParams requestParams = getPostCommentRequestParams(editText);
                setProgressVisible(true);
                new CommentsApiHelper().postComment(bearerAccessToken, requestParams, new IApiRequestComplete() {
                    @Override
                    public void onSuccess(Object response) {
                        GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.comment_category), context.getString(R.string.comment_added_action));
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

                });
            } else {
                editText.setText("");
                Toast.makeText(view.getContext(), R.string.empty_comment_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    private PostCommentRequestParams getPostCommentRequestParams(EditText editText) {
        PostCommentRequestParams requestParams = new PostCommentRequestParams();
        requestParams.setComment(editText.getText().toString().trim());
        editText.setText("");
        requestParams.setType(TYPE_FILTER);
        requestParams.setTargetId(recipeID);
        requestParams.setReplyId(null);
        return requestParams;
    }


    public void updateComments() {
        fetchCommentsFromDB();
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
