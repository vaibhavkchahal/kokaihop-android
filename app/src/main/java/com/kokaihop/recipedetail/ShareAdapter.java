package com.kokaihop.recipedetail;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ShareRowBinding;
import com.kokaihop.utility.ShareContents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 27/7/17.
 */

public class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> sharingList = new ArrayList<>();
    private Activity context;
    private String recipeTitle;
    private String recipeLink;
    private File shareFile;
    private ShareItemClickListener shareItemClickListener;

    public ShareAdapter(Activity context, List<Object> sharingList, ShareItemClickListener listener) {
        this.context = context;
        this.sharingList = sharingList;
        this.shareItemClickListener = listener;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRecipeLink(String recipeLink) {
        this.recipeLink = recipeLink;
    }

    public void setShareFile(File shareFile) {
        this.shareFile = shareFile;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_row, parent, false);
        return new ShareAdapter.ViewHolderShare(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolderShare holderShare = (ViewHolderShare) holder;
        final ShareContents shareContent = new ShareContents(context);
        TextView share = holderShare.binder.itemShare;
        Object object = sharingList.get(position);
        if (object instanceof ResolveInfo) {
            ResolveInfo info = (ResolveInfo) object;
            share.setText(info.activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            share.setCompoundDrawablesWithIntrinsicBounds(null, info.activityInfo.applicationInfo.loadIcon(context.getPackageManager()), null, null);
            shareContent.setPackageName(info.activityInfo.packageName);
            shareContent.setClassName(info.activityInfo.name);
        } else if (object instanceof ShareUsingPrint) {
            ShareUsingPrint shareUsingPrint = (ShareUsingPrint) object;
            share.setText(shareUsingPrint.getTitle());
            share.setCompoundDrawablesWithIntrinsicBounds(0, shareUsingPrint.getIcon(), 0, 0);
            shareContent.setPackageName(shareUsingPrint.getPackageName());
            shareContent.setClassName(shareUsingPrint.getClassName());
            shareContent.setIngredients(shareUsingPrint.getIngredients());
            shareContent.setDirections(shareUsingPrint.getDirections());
            shareContent.setRecipeDescription(shareUsingPrint.getRecipeDescription());
        }
        shareContent.setRecipeTitle(recipeTitle);
        shareContent.setRecipeLink(recipeLink);
        shareContent.setImageFile(shareFile);
        holderShare.binder.relativeLytShareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareItemClickListener.onShareItemClick();
                shareContent.shareCustom();
            }
        });
        holderShare.binder.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sharingList.size();
    }


    public class ViewHolderShare extends RecyclerView.ViewHolder {
        public ShareRowBinding binder;

        public ViewHolderShare(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    public interface ShareItemClickListener {
        void onShareItemClick();
    }
}


