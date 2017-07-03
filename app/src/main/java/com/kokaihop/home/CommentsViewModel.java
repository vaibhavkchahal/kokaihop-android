package com.kokaihop.home;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.comments.CommentsApiHelper;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.Payload;
import com.kokaihop.database.RecipeComment;
import com.kokaihop.database.RecipeCommentInfo;
import com.kokaihop.database.UserRealmObject;
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
    private int totalCommentCount;

    public int getTotalCommentCount() {
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

    RecyclerView recyclerView;

    public CommentsViewModel(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recipeDataManager = new RecipeDataManager();
//        fetchCommentsFromDB();
        fetchCommentFromServer(getOffset(), true);
    }

    public void fetchCommentFromServer(int offset, boolean progressVisibility) {
        setProgressVisible(progressVisibility);
        setOffset(offset);
        Map<String, String> map = new WeakHashMap<>();
        map.put("max", String.valueOf(getMax()));
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
                    totalCommentCount = json.getInt("totalItems");
                    JSONArray commentsJSONArray = json.getJSONArray("comments");
                    prepareCommentsModel(commentsJSONArray);
                    recyclerView.getAdapter().notifyDataSetChanged();
//                    recipeDataManager.updateRandomCommentsList(commentsJSONArray);
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

    private void prepareCommentsModel(JSONArray commentsJSONArray) throws JSONException {
        for (int i = 0; i < commentsJSONArray.length(); i++) {
            CommentRealmObject object = new CommentRealmObject();
            JSONObject jsonObject = commentsJSONArray.getJSONObject(i);
            object.set_id(jsonObject.getString("_id"));
            UserRealmObject userRealmObject = new UserRealmObject();
//            if (jsonObject.optJSONObject("sourceUser").optString("name") != null)
//                userRealmObject.setUserName(jsonObject.getJSONObject("sourceUser").getString("name"));
            userRealmObject.set_id(jsonObject.getJSONObject("sourceUser").getString("id"));
            object.setSourceUser(userRealmObject);
            JSONObject jsonObjectPayload = jsonObject.getJSONObject("payload");
            RecipeComment recipeComment = new RecipeComment();
            recipeComment.setContent(jsonObjectPayload.getJSONObject("comment").getString("content"));
            RecipeCommentInfo recipeCommentInfo = new RecipeCommentInfo();
            JSONObject jsonObjectRecipe = jsonObjectPayload.getJSONObject("recipe");
            recipeCommentInfo.setId(jsonObjectRecipe.getString("id"));
            recipeCommentInfo.setTitle(jsonObjectRecipe.getString("title"));
            if (jsonObjectRecipe.optJSONObject("createdBy") != null)
                recipeCommentInfo.setCreatorName(jsonObjectRecipe.optJSONObject("createdBy").optString("name"));
            if (jsonObjectRecipe.optJSONObject("mainImage") != null)
                recipeCommentInfo.setMainImagePublicId(jsonObjectRecipe.getJSONObject("mainImage").getString("publicId"));
            if (jsonObjectRecipe.optJSONObject("rating") != null) {
                recipeCommentInfo.setRecipeRating(Float.valueOf(jsonObjectRecipe.getJSONObject("rating").getString("average")));
                recipeCommentInfo.setRatersCount(jsonObjectRecipe.getJSONObject("rating").getInt("raters"));
            }
            Payload payload = new Payload();
            payload.setComment(recipeComment);
            payload.setRecipe(recipeCommentInfo);
            object.setPayload(payload);
            commentsList.add(object);
        }

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
