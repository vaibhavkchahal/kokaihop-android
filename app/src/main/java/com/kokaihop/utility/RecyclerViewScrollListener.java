package com.kokaihop.utility;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Vaibhav Chahal on 17/5/17.
 */

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int mVisibleThreshold = 2;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    private LinearLayoutManager mLayoutManager;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLayoutManager.getItemCount();
        mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        if (mFirstVisibleItem + mVisibleItemCount + mVisibleThreshold >= mTotalItemCount - mVisibleThreshold && dy > 0) {
            onLoadMore(recyclerView);
        }

    }

    public abstract void onLoadMore(RecyclerView recyclerView);
}