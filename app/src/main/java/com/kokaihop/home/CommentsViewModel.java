package com.kokaihop.home;

import android.app.Activity;
import android.view.View;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.comments.CommentsApiHelper;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;

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

public class CommentsViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 25;
    private final String TYPE_FILTER = "RECIPE_COMMENT";
    private RecipeDataManager recipeDataManager;
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

    public CommentsViewModel() {
        recipeDataManager = new RecipeDataManager();
//        fetchCommentsFromDB();
        fetchCommentFromServer(getOffset(), getMax(), true);
    }

    public void fetchCommentFromServer(int offset, int max, boolean progressVisibility) {
        setProgressVisible(progressVisibility);
        Map<String, String> map = new WeakHashMap<>();
        map.put("max", String.valueOf(max));
        map.put("offset", String.valueOf(offset));
        map.put("typeFilter", TYPE_FILTER);
        map.put("allRecipeDetails", String.valueOf(true));
        new CommentsApiHelper().fetchCommentsList(map, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                setProgressVisible(false);
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject json = new JSONObject(responseBody.string());
                    JSONArray commentsJSONArray = json.getJSONArray("comments");
                    recipeDataManager.updateRandomCommentsList(commentsJSONArray);
//                    fetchCommentsFromDB();
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
//    private void fetchCommentsFromDB() {
//        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
//        commentsList.clear();
//        commentsList.addAll(recipeRealmObject.getComments());
//        totalCommentCount = recipeRealmObject.getCounter().getComments();
//        commentListener.onUpdateCommentsList();
//    }
//    public void updateComments() {
//        fetchCommentsFromDB();
//    }

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
