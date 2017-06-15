package com.kokaihop.comments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowShowAllCommentsBinding;
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

    public ShowCommentRecyclerAdapter(List<CommentRealmObject> list) {
        commentsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_show_all_comments, parent, false);
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
        holderShowComments.binder.setModel(commentRealmObject);
        holderShowComments.binder.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    public class ViewHolderShowComments extends RecyclerView.ViewHolder {
        public RowShowAllCommentsBinding binder;

        public ViewHolderShowComments(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }
}