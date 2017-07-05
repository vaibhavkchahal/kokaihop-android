package com.kokaihop.comments;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import java.util.Map;

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

    public void fetchCommentsList(Map<String, String> map, final IApiRequestComplete successInterface) {
        Call<ResponseBody> commentsApiResponseCall = commentsApi.getCommentsList(map);
        commentsApiResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }

    public void postComment(String accessToken, PostCommentRequestParams requestParams, final IApiRequestComplete successInterface) {
        Call<ResponseBody> postCommentResponseCall = commentsApi.postComment(accessToken, requestParams);
        postCommentResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }

    public void fetchSingleCommentInfo(String commentId, final IApiRequestComplete successInterface) {
        Call<ResponseBody> commentResponseCall = commentsApi.getCommentInfo(commentId);
        commentResponseCall.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }
}
