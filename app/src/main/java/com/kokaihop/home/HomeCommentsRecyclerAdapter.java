package com.kokaihop.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ItemCommentHomeBinding;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/6/17.
 */

public class HomeCommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentRealmObject> commentsList = new ArrayList<>();
    private Context context;


    public HomeCommentsRecyclerAdapter(List<CommentRealmObject> list) {
        commentsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_home, parent, false);
        return new ViewHolderShowComments(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolderShowComments holderShowComments = (ViewHolderShowComments) holder;
        final CommentRealmObject commentRealmObject = commentsList.get(position);
        int commentUsetImageWidth = context.getResources().getDimensionPixelOffset(R.dimen.comment_card_logo_width);
        int commentUsetImageHeight = context.getResources().getDimensionPixelOffset(R.dimen.comment_card_logo_height);
        if (commentRealmObject.getPayload().getRecipe() != null) {
            if (commentRealmObject.getPayload().getRecipe().getCoverImage() != null) {
                String imageId = commentRealmObject.getPayload().getRecipe().getCoverImage();
                holderShowComments.binder.setRecipeImageUrl(CloudinaryUtils.getImageUrl(imageId, String.valueOf(commentUsetImageWidth), String.valueOf(commentUsetImageHeight)));
            }
        }
        holderShowComments.binder.setModel(commentRealmObject);
        holderShowComments.binder.setHandler(new RecipeHandler());
        holderShowComments.binder.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    public class ViewHolderShowComments extends RecyclerView.ViewHolder {
        public ItemCommentHomeBinding binder;

        public ViewHolderShowComments(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }
}