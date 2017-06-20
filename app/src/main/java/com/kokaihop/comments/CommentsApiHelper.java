package com.kokaihop.comments;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class CommentsApiHelper {

    private CommentsApi commentsApi;

    public CommentsApiHelper() {
        commentsApi = RetrofitClient.getInstance().create(CommentsApi.class);
    }

    public void fetchCommentsList(CommentRequestParams requestParams, final IApiRequestComplete successInterface) {
        Call<ResponseBody> commentsApiResponseCall = commentsApi.getCommentsList(requestParams.getMax(), requestParams.getOffset(), requestParams.getRecipeId(), requestParams.getTypeFilter());
        commentsApiResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }

    public void postComment(String accessToken,PostCommentRequestParams requestParams, final IApiRequestComplete successInterface) {
        Call<ResponseBody> postCommentResponseCall = commentsApi.postComment(accessToken,requestParams);
        postCommentResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }
}
