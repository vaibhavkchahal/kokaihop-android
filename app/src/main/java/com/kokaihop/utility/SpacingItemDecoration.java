package com.kokaihop.utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Vaibhav Chahal on 18/5/17.
 */

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int rightSpace;
    private int topSpace;
    private int bottomSpace;

    public SpacingItemDecoration(int left,int right,int top,int bottom) {
        this.leftSpace = left;
        this.rightSpace = right;
        this.topSpace = top;
        this.bottomSpace = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
        outRect.top = topSpace;
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.top = space;
//        } else {
//            outRect.top = 0;
//        }
    }
}
