package com.kokaihop.utility;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static com.kokaihop.feed.RecipeFeedViewModel.MAX_BADGE;

/**
 * Created by Vaibhav Chahal on 17/5/17.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        Logger.e("onScrolled", "onScrolled");


       /* visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItem + visibleItemCount + visibleThreshold >= totalItemCount - visibleThreshold && dy > 0) {
            onLoadMore(recyclerView);
        }*/

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                Logger.i("SCROLL_STATE_IDLE", RecyclerView.SCROLL_STATE_IDLE + "");


                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Logger.i("visibleItemCount", visibleItemCount + "");
                Logger.i("totalItemCount", totalItemCount + "");

                Logger.e("firstVisibleItem position", firstVisibleItem + "");
                if (totalItemCount > MAX_BADGE) {
                    Logger.i("onSyncDatabase", "onSyncDatabase");

                    Logger.i("lastVisibleItem position", layoutManager.findLastVisibleItemPosition() + "");
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    onSyncDatabase(recyclerView, lastVisibleItemPosition);

                } else if (firstVisibleItem + visibleItemCount + visibleThreshold >= totalItemCount - visibleThreshold) {


                    Logger.i("onLoadMore", "onLoadMore");
                    onLoadMore(recyclerView);

                }
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:

                break;

            case RecyclerView.SCROLL_STATE_SETTLING:

                break;

        }
    }

    public abstract void onLoadMore(RecyclerView recyclerView);

    {


    }

    public abstract void onSyncDatabase(RecyclerView recyclerView, int lastVisibleItemPosition);

    {


    }
}