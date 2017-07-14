package com.kokaihop.utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kokaihop.feed.FeedRecyclerAdapter;

/**
 * Created by Vaibhav Chahal on 18/5/17.
 */

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int rightSpace;
    private int topSpace;
    private int bottomSpace;

    public SpacingItemDecoration(int left, int right, int top, int bottom) {
        this.leftSpace = left;
        this.rightSpace = right;
        this.topSpace = top;
        this.bottomSpace = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int viewType = parent.getAdapter().getItemViewType(position);
        // Removed the Item decorator space because ads view type's visbility can be gone as well. so to set the padding dynamically if ads is visible.
        if (viewType == FeedRecyclerAdapter.TYPE_ITEM_ADVT) {
            outRect.setEmpty();
        } else {
            outRect.left = leftSpace;
            outRect.right = rightSpace;
            outRect.bottom = bottomSpace;
            outRect.top = topSpace;
        }


    }

}
