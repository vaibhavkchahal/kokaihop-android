package com.kokaihop.comments;

import android.app.Activity;
import android.view.View;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;

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

    private int offset = 3;
    private int max = 25;
    private final String typeFilter = "RECIPE_COMMENT";
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
        fetchCommentFromServer(getOffset(), getMax());
    }

    public void fetchCommentFromServer(int offset, int max) {
        setProgressVisible(true);
        CommentRequestParams requestParams = new CommentRequestParams(offset, max, recipeID, typeFilter);
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
//                    Logger.i("comment count -->", "" + recipeRealmObject.getComments().size());
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
        for (int i = 0; i < recipeRealmObject.getComments().size(); i++) {
            commentsList.add(0, recipeRealmObject.getComments().get(i));
        }
//        commentsList.addAll(recipeRealmObject.getComments());
        totalCommentCount = recipeRealmObject.getCounter().getComments();
        commentListener.onUpdateCommentsList();
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
