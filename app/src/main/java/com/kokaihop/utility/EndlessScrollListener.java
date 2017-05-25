package com.kokaihop.utility;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        switch (newState)
        {
            case RecyclerView.SCROLL_STATE_IDLE:
                Logger.e("SCROLL_STATE_IDLE",RecyclerView.SCROLL_STATE_IDLE+"");


                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Logger.e("visibleItemCount", visibleItemCount +"");
                Logger.e("totalItemCount", totalItemCount +"");
                Logger.e("firstVisibleItem position", firstVisibleItem +"");
                Logger.e("lastVisibleItem position", layoutManager.findLastVisibleItemPosition()+"");
                if (firstVisibleItem + visibleItemCount + visibleThreshold >= totalItemCount - visibleThreshold) {
                    onLoadMore(recyclerView);
                }
                else if(totalItemCount ==70)
                {
                    //TODO: data sync work
                }
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                Logger.e("SCROLL_STATE_DRAGGING",RecyclerView.SCROLL_STATE_DRAGGING+"");

                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                Logger.e("SCROLL_STATE_SETTLING",RecyclerView.SCROLL_STATE_DRAGGING+"");

                break;

        }
    }

    public abstract void onLoadMore(RecyclerView recyclerView);
    {


    }
}