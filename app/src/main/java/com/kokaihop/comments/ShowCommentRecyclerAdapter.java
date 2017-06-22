package com.kokaihop.comments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RecipeItemCommentBinding;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/6/17.
 */

public class ShowCommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentRealmObject> commentsList = new ArrayList<>();
    private Context context;
    private String comingFrom;

    public ShowCommentRecyclerAdapter(String comingFrom, List<CommentRealmObject> list) {
        commentsList = list;
        this.comingFrom = comingFrom;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_comment, parent, false);
        return new ShowCommentRecyclerAdapter.ViewHolderShowComments(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderShowComments holderShowComments = (ViewHolderShowComments) holder;
        CommentRealmObject commentRealmObject = commentsList.get(position);
        int commentUsetImageSize = context.getResources().getDimensionPixelOffset(R.dimen.imgview_comment_user_image_width);
        if (commentRealmObject.getSourceUser().getProfileImage() != null) {
            String commentUserImage = CloudinaryUtils.getRoundedImageUrl(commentRealmObject.getSourceUser().getProfileImage().getCloudinaryId(), String.valueOf(commentUsetImageSize), String.valueOf(commentUsetImageSize));
            holderShowComments.binder.setImageUrl(commentUserImage);
        }
        setReplyEvents(holderShowComments, commentRealmObject);
        if (comingFrom.contains("commentsSection") && !commentRealmObject.getPayload().getReplyEvents().isEmpty()) {
            holderShowComments.binder.relativeLayoutRepliedSection.setVisibility(View.VISIBLE);
        } else {
            holderShowComments.binder.relativeLayoutRepliedSection.setVisibility(View.GONE);
        }
        holderShowComments.binder.relativeLayoutComment.setClickable(false);
        holderShowComments.binder.setModel(commentRealmObject);
        holderShowComments.binder.setHandler(new CommentsHandler());
        holderShowComments.binder.executePendingBindings();
    }

    private void setReplyEvents(ViewHolderShowComments holderShowComments, CommentRealmObject commentRealmObject) {
        if (commentRealmObject.getPayload().getReplyEvents().size() > 0) {
            CommentRealmObject replyCommentRealmObject = commentRealmObject.getPayload().getReplyEvents().get(0);
            int replyCommentUsetImageSize = context.getResources().getDimensionPixelOffset(R.dimen.reply_user_image_height_width);
            if (commentRealmObject.getSourceUser().getProfileImage() != null) {
                String replyCommentUserImage = CloudinaryUtils.getRoundedImageUrl(replyCommentRealmObject.getSourceUser().getProfileImage().getCloudinaryId(), String.valueOf(replyCommentUsetImageSize), String.valueOf(replyCommentUsetImageSize));
                holderShowComments.binder.setReplyUserImageUrl(replyCommentUserImage);
            }
            String replyUserName = replyCommentRealmObject.getSourceUser().getUserName();
            holderShowComments.binder.setLatestCommentUsername(replyUserName);

        }
    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    public class ViewHolderShowComments extends RecyclerView.ViewHolder {
        public RecipeItemCommentBinding binder;

        public ViewHolderShowComments(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }
}